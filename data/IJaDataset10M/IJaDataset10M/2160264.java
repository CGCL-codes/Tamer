package cs437;

import com.trolltech.qt.QSignalEmitter.Signal4;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.xml.*;

public class TalentTree extends QLabel {

    public QGraphicsView view;

    private QGraphicsScene scene;

    private static QSizeF treeSize = new QSizeF(240, 600);

    private int pointsSpent;

    public int maxPoints;

    public Signal1<Integer> pointSpent = new Signal1<Integer>();

    public Signal1<Integer> P = new Signal1<Integer>();

    private static final int maxTier = 9;

    QPointF[][] tierCords;

    Talent[][] talentGrid = new Talent[10][5];

    boolean[] tierActive = new boolean[10];

    int[] tierpoints = new int[10];

    public Signal1<Talent> hovering = new Signal1<Talent>();

    public TalentTree(QWidget parent) {
        super(parent);
        setUpCords();
    }

    public TalentTree(QWidget parent, String fileName, int tab) {
        super(parent);
        this.setFrameShape(Shape.StyledPanel);
        setUpCords();
        setUpTree(fileName, tab);
    }

    public void setUpCords() {
        float[] xCords = { 5, 65, 125, 185 };
        float[] yCords = { 0, 70, 140, 210, 280, 350, 420, 490, 560 };
        tierCords = new QPointF[10][5];
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 5; j++) {
                tierCords[i][j] = new QPointF(xCords[j - 1], yCords[i - 1]);
            }
        }
    }

    private void setUpTree(String xmlFile, int tree) {
        QRectF rect = new QRectF(new QPointF(0, 0), treeSize);
        scene = new QGraphicsScene(rect);
        String className;
        QDomDocument qDoc = new QDomDocument(xmlFile);
        QFile file = new QFile(xmlFile);
        qDoc = new QDomDocument("Class");
        qDoc.setContent(file);
        if (!file.isReadable()) {
            System.exit(0);
        }
        file.close();
        QDomElement talentNode = new QDomElement();
        QDomElement qElm = new QDomElement(qDoc.documentElement());
        className = qElm.attribute("name");
        QDomNodeList nodeList;
        String nodeName;
        String name, iconPath;
        int ranks, tier, pos;
        QDomNamedNodeMap attributes;
        Talent t;
        nodeList = qElm.elementsByTagName("tree");
        for (int k = 0; k < nodeList.count(); k++) {
            qElm = nodeList.item(k).toElement();
            if (qElm.attribute("number").equals(String.valueOf(tree))) {
                QDomNodeList talents = qElm.childNodes();
                for (int i = 0; i < talents.count(); i++) {
                    if (talents.item(i).isElement() && talents.item(i).nodeName().equals("talent")) {
                        talentNode = talents.item(i).toElement();
                        nodeName = talentNode.nodeName();
                        attributes = talentNode.attributes();
                        QDomNodeList rankList = new QDomNodeList(talentNode.childNodes());
                        name = attributes.namedItem("name").toAttr().value();
                        ranks = Integer.parseInt(attributes.namedItem("ranks").toAttr().value());
                        tier = Integer.parseInt(attributes.namedItem("tier").toAttr().value());
                        iconPath = "img/" + className + "/" + tree + "/" + attributes.namedItem("icon").toAttr().value();
                        pos = Integer.parseInt(attributes.namedItem("pos").toAttr().value());
                        t = new Talent(name, ranks, tier, pos, iconPath);
                        talentGrid[tier][pos] = t;
                        scene.addItem(t);
                        t.setZValue(-i * 10);
                        t.setPos(tierCords[tier][pos]);
                        t.pointSpent.connect(this, "pointChange(int, int, int)");
                        t.hovering.connect(this, "catchHover(Talent)");
                        for (int j = 0; j < rankList.count(); j++) {
                            QDomElement rank = rankList.item(j).toElement();
                            if (rank.nodeName().equals("child")) {
                                String ctier, cpos;
                                ctier = rank.attribute("tier");
                                cpos = rank.attribute("pos");
                                t.addChild(Integer.parseInt(ctier), Integer.parseInt(cpos));
                            }
                            if (rank.nodeName().equals("parent")) {
                                String ctier, cpos;
                                ctier = rank.attribute("tier");
                                cpos = rank.attribute("pos");
                                t.setParent(Integer.parseInt(ctier), Integer.parseInt(cpos));
                            }
                            if (rank.nodeName().equals("rank")) {
                                t.addRank(rank.text());
                            }
                            if (rank.nodeName().equals("info")) {
                            }
                        }
                    }
                }
            }
        }
        view = new QGraphicsView(scene);
        view.setBackgroundBrush(new QBrush(new QPixmap("img/" + className + "/" + tree + "/" + "bg.png")));
        view.setRenderHint(QPainter.RenderHint.Antialiasing);
        view.setDragMode(QGraphicsView.DragMode.RubberBandDrag);
        view.setCacheMode(QGraphicsView.CacheModeFlag.CacheBackground);
        view.setMinimumHeight(600);
        view.setMaximumHeight(620);
        view.setMinimumWidth(220);
        view.setMaximumWidth(250);
        QGridLayout layout = new QGridLayout();
        layout.addWidget(view);
        setLayout(layout);
    }

    void pointChange(int tier, int pos, int change) {
        Talent t = talentGrid[tier][pos];
        if ((pointsSpent == maxPoints) && (change > 0)) {
            t.undoRU();
            System.out.println("maxpoints: " + String.valueOf(maxPoints));
        } else if (change > 0) {
            ++pointsSpent;
            System.out.println("Point Spent on item: " + t.getName());
            tierpoints[t.getTier()]++;
            switch(pointsSpent) {
                case 5:
                    activateTier(2);
                    break;
                case 10:
                    activateTier(3);
                    break;
                case 15:
                    activateTier(4);
                    break;
                case 20:
                    activateTier(5);
                    break;
                case 25:
                    activateTier(6);
                    break;
                case 30:
                    activateTier(7);
                    break;
                case 35:
                    activateTier(8);
                    break;
                case 40:
                    activateTier(9);
                    break;
            }
            if (t.isDependent) {
                talentGrid[t.parent.first][t.parent.second].lock();
            }
            if (t.hasDependency && t.isMaxed()) {
                for (int i = 0; i < t.child.size(); i++) {
                    if (tierActive[t.child.get(i).first]) talentGrid[t.child.get(i).first][t.child.get(i).second].activate();
                }
            }
            pointSpent.emit(1);
        } else if (change < 0) {
            System.out.println("Point Un-Spent on item: " + t.getName());
            int pointsBelow = pointsBelow(tier);
            int pointsAbove = pointsAbove(tier);
            int maxTier = maxTier();
            int attemptedPoints = pointsSpent - 1 - tierpoints[maxTier];
            int tierAbove = t.getTier() + 1;
            int tierCheck = pointsSpent / 5 + 1;
            int minPoints = (maxTier - 1) * 5;
            boolean clear = checkDown(t.getTier());
            System.out.println("Checking Tier: " + String.valueOf(tierCheck));
            if (pointsBelow(t.getTier()) > t.getTier() * 5) {
                System.out.println("Denied: Points left in tiers above #" + String.valueOf(pointsAbove));
                System.out.println("Points Below: " + String.valueOf(pointsBelow));
                t.undo();
            } else if (tierpoints[t.getTier()] < 5 && pointsAbove > 0) {
                System.out.println("Dexcvbgxcbxcvnied: Points left in tier " + String.valueOf(tierAbove));
                t.undo();
            } else if (pointsSpent < 41 && minPoints % 5 == 0 && tierpoints[tierCheck] > 0 && tierCheck != t.getTier()) {
                String warning = "Illegal: " + String.valueOf(pointsSpent) + " -> " + String.valueOf(pointsSpent - 1) + " Would cause tier " + String.valueOf(tierCheck) + " to deactivate but it has " + String.valueOf(tierpoints[tierCheck] + " left");
                System.out.println(warning);
                t.undo();
            } else {
                if (pointsSpent % 5 == 0) deActivateTier(tierCheck);
                tierpoints[t.getTier()]--;
                pointsSpent--;
                pointSpent.emit(-1);
            }
            if (t.isDependent && t.isEmpty()) {
                talentGrid[t.parent.first][t.parent.second].unLock();
            }
        }
    }

    private void activateTier(int t) {
        Talent current;
        System.out.println("Activating tier " + String.valueOf(t));
        for (int i = 1; i < 5; i++) {
            if (talentGrid[t][i] != null) {
                current = talentGrid[t][i];
                if (!current.isDependent || talentGrid[current.parent.first][current.parent.second].isMaxed()) current.activate();
            }
        }
        tierActive[t] = true;
    }

    private boolean deActivateTier(int t) {
        boolean conflict = false;
        for (int i = 1; i < 5; i++) {
            QGraphicsItemInterface c = scene.itemAt(tierCords[t][i]);
            Talent current = ((Talent) c);
            if (current != null) {
                if (!current.isEmpty()) {
                    System.out.println("Conflict: Can not disable tier " + String.valueOf(current.getTier()) + ". Points remain in item " + current.getName());
                    conflict = true;
                }
                current.deActivate();
                tierActive[t] = false;
            }
        }
        return conflict;
    }

    private boolean checkDown(int t) {
        int tier = t;
        int pointsBelow = pointsBelow(tier);
        int pointsAbove = pointsAbove(tier);
        int maxTier = maxTier();
        int requiredPoints = (maxTier - 1) * 5;
        int attemptedPoints = pointsSpent - 1 - tierpoints[maxTier];
        if (attemptedPoints < requiredPoints) {
            return false;
        }
        return true;
    }

    public void mousePressEvent(QMouseEvent event) {
        if (event.button() == Qt.MouseButton.LeftButton) {
            System.out.println("Tree:Left Mouse Clicked");
            event.accept();
        }
        if (event.button() == Qt.MouseButton.RightButton) {
            System.out.println("Tree:Right Mouse Clicked");
            event.accept();
        }
    }

    private int pointsBelow(int tier) {
        int count = 0;
        for (int i = tier - 1; i > 0; i--) {
            count += tierpoints[i];
        }
        return count;
    }

    private int pointsAbove(int tier) {
        int count = 0;
        for (int i = tier + 1; i < 10; i++) {
            count += tierpoints[i];
        }
        return count;
    }

    private int maxTier() {
        int max = 0;
        int tier = 0;
        for (int i = 1; i < 10; i++) {
            if (tierpoints[i] > 0) tier = i;
        }
        return tier;
    }

    public int getPointsSpent() {
        return pointsSpent;
    }

    public void setMaxPoints(int x) {
        this.maxPoints = x;
    }

    public void catchHover(Talent t) {
        hovering.emit(t);
    }
}
