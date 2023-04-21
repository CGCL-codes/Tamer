package unbbayes.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import amplia.gui.AmpliaNodeWindow;
import unbbayes.controller.MainController;
import unbbayes.controller.NetworkController;
import unbbayes.draw.DrawDashRectangle;
import unbbayes.draw.DrawElement;
import unbbayes.draw.IDrawable;
import unbbayes.prs.Edge;
import unbbayes.prs.Node;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.prs.bn.SingleEntityNetwork;
import unbbayes.prs.mebn.MultiEntityNode;
import unbbayes.prs.mebn.exception.CycleFoundException;
import unbbayes.prs.mebn.exception.MEBNConstructionException;
import unbbayes.prs.mebn.exception.MFragDoesNotExistException;
import unbbayes.util.GeometricUtil;
import unbbayes.util.NodeList;

/**
 *  Essa classe � respons�vel por desenhar a rede Bayesiana ou a MFrag na tela. Ela extende a classe
 *  <code>JPanel</code>. Ela tamb�m implementa
 *  as interfaces MouseListener e MouseMotionListener, para poder tratar os eventos de
 *  mouse e desenhar a rede Bayesiana.
 *
 *@author     Michael S. Onishi
 *@author     Rommel N. Carvalho
 *@created    27 de Junho de 2001
 *@see        JPanel
 */
public class GraphPane extends JPanel implements MouseListener, MouseMotionListener {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    private NetworkController controller;

    private List<Edge> edgeList;

    private NodeList nodeList;

    private List<IDrawable> selectedGroup;

    private IDrawable selected;

    private Graphics2D graphBoard;

    private Point2D.Double startSelectionPoint;

    private Point2D.Double endSelectionPoint;

    private DrawElement drawSelection;

    private GraphAction action;

    private Node movingNode;

    private Edge movingEdge;

    private boolean bMoveEdge;

    private boolean bMoveNode;

    private static Color selectionColor;

    private static Color backgroundColor;

    private JViewport graphViewport;

    private Dimension visibleDimension;

    private Dimension graphDimension;

    private int direction;

    private JPopupMenu popup = new JPopupMenu();

    /** Load resource file from this package */
    private static ResourceBundle resource = ResourceBundle.getBundle("unbbayes.gui.resources.GuiResources");

    /**
	 *  O construtor � respons�vel por iniciar todas as vari�veis que ser�o
	 *  utilizadas por essa classe para que se possa desenhar a rede Bayesiana.
	 *
	 *@param  controlador  o controlador (<code>TControladorTelaPrincipal</code>)
	 *@param  graphViewport a tela, (<code>TViewport</code>), onde ser� inserida essa classe
	 */
    public GraphPane(final NetworkController controller, JViewport graphViewport) {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(controller);
        this.controller = controller;
        this.setRequestFocusEnabled(true);
        this.graphViewport = graphViewport;
        this.setSize(800, 600);
        edgeList = controller.getGraph().getEdges();
        nodeList = controller.getGraph().getNodes();
        selectedGroup = new ArrayList<IDrawable>();
        startSelectionPoint = new Point2D.Double();
        endSelectionPoint = new Point2D.Double();
        drawSelection = new DrawDashRectangle(startSelectionPoint, endSelectionPoint);
        bMoveEdge = false;
        bMoveNode = false;
        selectionColor = Color.red;
        backgroundColor = Color.white;
        graphDimension = new Dimension(1500, 1500);
        visibleDimension = new Dimension(0, 0);
        action = GraphAction.NONE;
        JMenuItem item = new JMenuItem(resource.getString("properties"));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                controller.showExplanationProperties((ProbabilisticNode) getSelected());
            }
        });
        popup.add(item);
    }

    /**
	 *  Seta o atributo corSelecao (cor de sele��o) do objeto da classe GraphPane
	 *
	 *@param  selectionColor  A nova cor, <code>Color</code>, de sele��o
	 *@see Color
	 */
    public static void setSelectionColor(Color selectionColor) {
        GraphPane.selectionColor = selectionColor;
        DrawElement.setSelectionColor(selectionColor);
    }

    /**
	 *  Seta o atributo corFundo (cor de fundo) do objeto da classe GraphPane
	 *
	 *@param  backgroundColor  A nova cor, <code>Color</code>, de Fundo
	 *@see Color
	 */
    public void setBackgroundColor(Color backColor) {
        GraphPane.backgroundColor = backColor;
    }

    /**
	 *  Seta o atributo graphDimension (tamanho da rede Bayesiana) do objeto da classe GraphPane
	 *
	 *@param  graphDimension  O novo valor do tamanho da rede (<code>Dimension</code>)
	 *@see Dimension
	 */
    public void setGraphDimension(Dimension graphDimension) {
        this.graphDimension = graphDimension;
    }

    /**
	 *  Retorna a cor de fundo
	 *
	 *@return    a backgroundColor (<code>Color</code>)
	 *@see Color
	 */
    public Color getBackgroundColor() {
        return GraphPane.backgroundColor;
    }

    /**
	 *  Retorna a cor de sele��o
	 *
	 *@return    a selectionColor (<code>Color</code>)
	 *@see Color
	 */
    public static Color getSelectionColor() {
        return GraphPane.selectionColor;
    }

    /**
	 *  Retorna o objeto selecionado (<code>Object</code>), que pode ser um <code>Node</code> ou <code>Edge</code>
	 *
	 *@return    O valor do <code>Object</code> selected
	 *@see Object
	 *@see Node
	 *@see Edge
	 */
    public Object getSelected() {
        return this.selected;
    }

    /**
	 *  Retorna uma lista de selecionados (<code>List</code>), que podem ser um <code>Node</code> e/ou <code>Edge</code>
	 *
	 *@return    O valor do <code>List</code> selectedGroup
	 *@see List
	 *@see Node
	 *@see Edge
	 */
    public List getSelectedGroup() {
        return this.selectedGroup;
    }

    /**
	 *  Pega a <code>Dimension</code> tamanhoRede do objeto da classe GraphPane
	 *
	 *@return    A <code>Dimension</code> graphDimension
	 */
    public Dimension getGraphDimension() {
        return this.graphDimension;
    }

    /**
	 *  Pega o n� que se encontra na posi��o x,y
	 *
	 *@param  x  A posi��o x (double)
	 *@param  y  A posi��o y (double)
	 *@return    O n� encontrado (<code>Node</code>)
	 *@see Node
	 */
    public Node getNode(double x, double y) {
        double x1;
        double y1;
        long width = Node.getWidth() / 2;
        long height = Node.getHeight() / 2;
        for (int i = 0; i < nodeList.size(); i++) {
            Node nodeAux = nodeList.get(i);
            x1 = nodeAux.getPosition().getX();
            y1 = nodeAux.getPosition().getY();
            if ((x >= x1 - width) && (x <= x1 + width) && (y >= y1 - height) && (y <= y1 + height)) {
                return nodeAux;
            }
        }
        return null;
    }

    /**
	 *  Pega o atributo focusTransversable do objeto da classe GraphPane
	 *
	 *@return    True como valor do focusTransversable (m�todo necess�rio para que se possa tratar evento de tecla)
	 */
    public boolean isFocusable() {
        return true;
    }

    /**
	 *  Pega o arco que se encontra na posi��o x,y
	 *
	 *@param  x  A posi��o x (double)
	 *@param  y  A posi��o y (double)
	 *@return    O arco encontrado (<code>Edge</code>)
	 *@see Edge
	 */
    public Edge getEdge(double x, double y) {
        double x1;
        double y1;
        double x2;
        double y2;
        for (int i = 0; i < edgeList.size(); i++) {
            Edge arcoPegar = (Edge) edgeList.get(i);
            x1 = arcoPegar.getOriginNode().getPosition().getX();
            y1 = arcoPegar.getOriginNode().getPosition().getY();
            x2 = arcoPegar.getDestinationNode().getPosition().getX();
            y2 = arcoPegar.getDestinationNode().getPosition().getY();
            double yTeste = ((y2 - y1) / (x2 - x1)) * x + (y1 - x1 * ((y2 - y1) / (x2 - x1)));
            double xTeste = (y - (y1 - x1 * ((y2 - y1) / (x2 - x1)))) / ((y2 - y1) / (x2 - x1));
            Node no1 = arcoPegar.getOriginNode();
            Node no2 = arcoPegar.getDestinationNode();
            Point2D.Double ponto1 = GeometricUtil.getCircunferenceTangentPoint(no1.getPosition(), no2.getPosition(), (Node.getWidth() + Node.getHeight()) / 4);
            Point2D.Double ponto2 = GeometricUtil.getCircunferenceTangentPoint(no2.getPosition(), no1.getPosition(), (Node.getWidth() + Node.getHeight()) / 4);
            if (ponto1.getX() < ponto2.getX()) {
                if (((y <= yTeste + 5) && (y >= yTeste - 5)) || ((x <= xTeste + 5) && (x >= xTeste - 5))) {
                    if (ponto1.getY() < ponto2.getY()) {
                        if ((y >= ponto1.getY() - 5) && (y <= ponto2.getY() + 5) && (x >= ponto1.getX() - 5) && (x <= ponto2.getX() + 5)) {
                            return arcoPegar;
                        }
                    } else {
                        if ((y >= ponto2.getY() - 5) && (y <= ponto1.getY() + 5) && (x >= ponto1.getX() - 5) && (x <= ponto2.getX() + 5)) {
                            return arcoPegar;
                        }
                    }
                }
            } else {
                if (((y <= yTeste + 5) && (y >= yTeste - 5)) || ((x <= xTeste + 5) && (x >= xTeste - 5))) {
                    if (ponto1.getY() < ponto2.getY()) {
                        if ((y >= ponto1.getY() - 5) && (y <= ponto2.getY() + 5) && (x >= ponto2.getX() - 5) && (x <= ponto1.getX() + 5)) {
                            return arcoPegar;
                        }
                    } else {
                        if ((y >= ponto2.getY() - 5) && (y <= ponto1.getY() + 5) && (x >= ponto2.getX() - 5) && (x <= ponto1.getX() + 5)) {
                            return arcoPegar;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
	 *  Pega o atributo graphViewport (<code>JViewport</code>) do objeto da classe GraphPane
	 *
	 *@return    O graphViewport (container onde essa classe se encontra inserida)
	 *@see JViewport
	 */
    public JViewport getGraphViewport() {
        return this.graphViewport;
    }

    /**
	 *  Pega o maior ponto (<code>Point2D.Double</code>) do objeto da classe GraphPane
	 *
	 *@return    O maior ponto (x,y) existente nessa rede Bayesiana
	 *@see Point2D.Double
	 */
    public Point2D.Double getBiggestPoint() {
        double maiorX = 0;
        double maiorY = 0;
        long width = Node.getWidth() / 2;
        long height = Node.getHeight() / 2;
        for (int i = 0; i < nodeList.size(); i++) {
            Node noAux = (Node) nodeList.get(i);
            if (maiorX < noAux.getPosition().getX() + width) {
                maiorX = noAux.getPosition().getX() + width;
            }
            if (maiorY < noAux.getPosition().getY() + height) {
                maiorY = noAux.getPosition().getY() + height;
            }
        }
        if (maiorX < visibleDimension.getWidth()) {
            maiorX = graphViewport.getViewSize().getWidth();
        }
        if (maiorY < visibleDimension.getHeight()) {
            maiorY = graphViewport.getViewSize().getHeight();
        }
        return new Point2D.Double(maiorX, maiorY);
    }

    /**
	 *  Pega o tamanho necess�rio para repintar a tela (<code>Rectangle</code>)
	 *
	 *@return    O tamanho necess�rio para repintar a tela
	 *@see Rectangle
	 */
    public Rectangle getRectangleRepaint() {
        double maiorX;
        double maiorY;
        double menorX;
        double menorY;
        if (bMoveNode && selected instanceof Node) {
            Node noAux = (Node) selected;
            maiorX = noAux.getPosition().getX();
            menorX = noAux.getPosition().getX();
            maiorY = noAux.getPosition().getY();
            menorY = noAux.getPosition().getY();
            Node noAux2;
            for (int i = 0; i < noAux.getParents().size(); i++) {
                noAux2 = (Node) noAux.getParents().get(i);
                if (maiorX < noAux2.getPosition().getX()) {
                    maiorX = noAux2.getPosition().getX();
                } else {
                    if (menorX > noAux2.getPosition().getX()) {
                        menorX = noAux2.getPosition().getX();
                    }
                }
                if (maiorY < noAux2.getPosition().getY()) {
                    maiorY = noAux2.getPosition().getY();
                } else {
                    if (menorY > noAux2.getPosition().getY()) {
                        menorY = noAux2.getPosition().getY();
                    }
                }
            }
            for (int i = 0; i < noAux.getChildren().size(); i++) {
                noAux2 = (Node) noAux.getChildren().get(i);
                if (maiorX < noAux2.getPosition().getX()) {
                    maiorX = noAux2.getPosition().getX();
                } else {
                    if (menorX > noAux2.getPosition().getX()) {
                        menorX = noAux2.getPosition().getX();
                    }
                }
                if (maiorY < noAux2.getPosition().getY()) {
                    maiorY = noAux2.getPosition().getY();
                } else {
                    if (menorY > noAux2.getPosition().getY()) {
                        menorY = noAux2.getPosition().getY();
                    }
                }
            }
            long width = Node.getWidth() / 2;
            long height = Node.getHeight() / 2;
            return new Rectangle((int) (menorX - 6 * width), (int) (menorY - 6 * height), (int) (maiorX - menorX + 12 * width), (int) (maiorY - menorY + 12 * height));
        } else {
            return new Rectangle((int) controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue(), (int) controller.getScreen().getJspGraph().getVerticalScrollBar().getValue(), (int) visibleDimension.getWidth(), (int) visibleDimension.getHeight());
        }
    }

    /**
	 *  M�todo respons�vel por repintar a rede Bayesiana
	 */
    public void update() {
        this.repaint(getRectangleRepaint());
    }

    /**
	 *  M�todo respons�vel por tratar o evento de bot�o de mouse pressionado
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mousePressed(MouseEvent e) {
        graphViewport.setScrollMode(JViewport.BLIT_SCROLL_MODE);
        if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
            Node node = getNode(e.getX(), e.getY());
            Edge arc = getEdge(e.getX(), e.getY());
            switch(getAction()) {
                case CREATE_PROBABILISTIC_NODE:
                    controller.insertProbabilisticNode(e.getX(), e.getY());
                    return;
                case CREATE_DECISION_NODE:
                    controller.insertDecisionNode(e.getX(), e.getY());
                    return;
                case CREATE_UTILITY_NODE:
                    controller.insertUtilityNode(e.getX(), e.getY());
                    return;
                case CREATE_DOMAIN_MFRAG:
                    controller.insertDomainMFrag();
                    return;
                case CREATE_CONTEXT_NODE:
                    try {
                        controller.insertContextNode(e.getX(), e.getY());
                        node = getNode(e.getX(), e.getY());
                        selectObject(node);
                        controller.selectNode(node);
                    } catch (MEBNConstructionException exception) {
                        JOptionPane.showMessageDialog(controller.getScreen().getMebnEditionPane(), resource.getString("withoutMFrag"), resource.getString("operationError"), JOptionPane.WARNING_MESSAGE);
                    }
                    return;
                case CREATE_RESIDENT_NODE:
                    try {
                        controller.insertResidentNode(e.getX(), e.getY());
                        node = getNode(e.getX(), e.getY());
                        selectObject(node);
                        controller.selectNode(node);
                    } catch (MEBNConstructionException exception) {
                        JOptionPane.showMessageDialog(controller.getScreen().getMebnEditionPane(), resource.getString("withoutMFrag"), resource.getString("operationError"), JOptionPane.WARNING_MESSAGE);
                    }
                    return;
                case CREATE_INPUT_NODE:
                    try {
                        controller.insertInputNode(e.getX(), e.getY());
                        node = getNode(e.getX(), e.getY());
                        selectObject(node);
                        controller.selectNode(node);
                    } catch (MFragDoesNotExistException exception) {
                        JOptionPane.showMessageDialog(controller.getScreen().getMebnEditionPane(), resource.getString("withoutMFrag"), resource.getString("operationError"), JOptionPane.WARNING_MESSAGE);
                    }
                    return;
                case CREATE_ORDINARYVARIABLE_NODE:
                    try {
                        controller.getMebnController().insertOrdinaryVariable(e.getX(), e.getY());
                        node = getNode(e.getX(), e.getY());
                        selectObject(node);
                        controller.selectNode(node);
                    } catch (MEBNConstructionException exception) {
                        JOptionPane.showMessageDialog(controller.getScreen().getMebnEditionPane(), resource.getString("withoutMFrag"), resource.getString("operationError"), JOptionPane.WARNING_MESSAGE);
                    }
                    return;
                case CREATE_EDGE:
                    if (node != null) {
                        bMoveEdge = true;
                        Node node2;
                        if (controller.getMebnController() != null) {
                            node2 = new MultiEntityNode();
                        } else {
                            node2 = new ProbabilisticNode();
                        }
                        node2.setPosition(e.getX(), e.getY());
                        movingEdge = new Edge(node, node2);
                        movingEdge.setNew(true);
                    }
                    return;
                case SELECT_MANY_OBJECTS:
                    startSelectionPoint.setLocation(e.getX(), e.getY());
                    endSelectionPoint.setLocation(e.getX(), e.getY());
                    return;
                case NONE:
                    if (node != null) {
                        if (!node.isSelected()) {
                            selectObject(node);
                            controller.selectNode(node);
                            if (controller.getScreen().isCompiled()) {
                                for (int i = 0; i < controller.getScreen().getEvidenceTree().getRowCount(); i++) {
                                    if (controller.getScreen().getEvidenceTree().getPathForRow(i).getLastPathComponent().toString().equals(selected.toString())) {
                                        controller.getScreen().getEvidenceTree().setSelectionPath(controller.getScreen().getEvidenceTree().getPathForRow(i));
                                        break;
                                    }
                                }
                            }
                        }
                        if (selectedGroup.size() == 0) {
                            movingNode = node;
                            bMoveNode = true;
                            setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        }
                    } else if (arc != null && !arc.isSelected()) {
                        selectObject(arc);
                    }
                    return;
            }
        } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            setAction(GraphAction.NONE);
        }
        this.repaint((int) controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue(), (int) controller.getScreen().getJspGraph().getVerticalScrollBar().getValue(), (int) visibleDimension.getWidth(), (int) visibleDimension.getHeight());
    }

    /**
	 *  M�todo respons�vel por tratar o evento de clique no bot�o do mouse
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mouseClicked(MouseEvent e) {
        this.requestFocus();
        Node node = getNode(e.getX(), e.getY());
        if (node != null) {
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                if (e.getClickCount() == 2) {
                    AmpliaNodeWindow janela = new AmpliaNodeWindow(graphViewport.getSize(), node, nodeList);
                    janela = null;
                }
                if (controller.getGraph() instanceof SingleEntityNetwork) {
                    controller.getScreen().setTable(controller.makeTable(node));
                    controller.getScreen().setTableOwner(node);
                    if (controller.getScreen().isCompiled()) {
                        for (int i = 0; i < controller.getScreen().getEvidenceTree().getRowCount(); i++) {
                            if (controller.getScreen().getEvidenceTree().getPathForRow(i).getLastPathComponent().toString().equals(selected.toString())) {
                                if (controller.getScreen().getEvidenceTree().isExpanded(controller.getScreen().getEvidenceTree().getPathForRow(i))) {
                                    controller.getScreen().getEvidenceTree().collapsePath(controller.getScreen().getEvidenceTree().getPathForRow(i));
                                } else {
                                    controller.getScreen().getEvidenceTree().expandPath(controller.getScreen().getEvidenceTree().getPathForRow(i));
                                }
                                break;
                            }
                        }
                    }
                } else {
                }
            }
        } else {
            Edge edge = getEdge(e.getX(), e.getY());
            if ((edge != null) && (e.getModifiers() == MouseEvent.BUTTON1_MASK) && (e.getClickCount() == 2)) {
                if ((direction == 0) || (direction == 1)) {
                    direction++;
                    edge.setDirection(true);
                    edge.changeDirection();
                } else if (direction == 2) {
                    direction = 0;
                    edge.setDirection(false);
                }
            }
        }
    }

    /**
	 *  M�todo respons�vel por tratar o evento de bot�o de mouse soltado
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mouseReleased(MouseEvent e) {
        Node destinationNode = getNode(e.getX(), e.getY());
        Edge edge = getEdge(e.getX(), e.getY());
        switch(getAction()) {
            case CREATE_EDGE:
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    Node originNode = movingEdge.getOriginNode();
                    if ((destinationNode != null) && !originNode.equals(destinationNode) && (controller.getGraph().hasEdge(originNode, destinationNode) == -1)) {
                        movingEdge = new Edge(originNode, destinationNode);
                        insertEdge(movingEdge);
                    }
                    movingEdge = null;
                }
                break;
            case SELECT_MANY_OBJECTS:
                endSelectionPoint.setLocation(e.getX(), e.getY());
                setSelectedGroup(startSelectionPoint, endSelectionPoint);
                startSelectionPoint.setLocation(-1, -1);
                endSelectionPoint.setLocation(-1, -1);
                break;
            case NONE:
                bMoveNode = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if ((selected == null) || (destinationNode == null || !selected.equals(destinationNode)) && (edge == null || !selected.equals(edge))) {
                    unselectAll();
                    controller.unselectAll();
                }
                break;
        }
        if (bMoveEdge) {
            bMoveEdge = false;
        }
        if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            setAction(GraphAction.NONE);
        }
        update();
        if (e.isPopupTrigger() && (getSelected() != null)) {
            if (!(getSelected() instanceof Edge)) {
                if (((Node) getSelected()).getInformationType() == Node.EXPLANATION_TYPE) {
                    popup.setEnabled(true);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    /**
	 *  M�todo respons�vel por tratar o evento do mouse entrar nesse componente (objeto da classe GraphPane)
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mouseEntered(MouseEvent e) {
        if ((!bMoveNode) && (!bMoveEdge)) {
            visibleDimension = new Dimension((int) (controller.getScreen().getJspGraph().getSize().getWidth()), (int) (controller.getScreen().getJspGraph().getSize().getHeight()));
            graphViewport.setOpaque(true);
            graphViewport.scrollRectToVisible(new Rectangle(graphDimension));
            graphViewport.setPreferredSize(graphDimension);
            graphViewport.revalidate();
        }
    }

    /**
	 *  M�todo respons�vel por tratar o evento do mouse sair desse componente (objeto da classe GraphPane)
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mouseExited(MouseEvent e) {
    }

    /**
	 *  M�todo respons�vel por tratar o evento arrastar o mouse com o bot�o pressionado
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mouseDragged(MouseEvent e) {
        if (getAction() == GraphAction.SELECT_MANY_OBJECTS) {
            updateEndSelectionPoint(e.getX(), e.getY());
        }
        long width = Node.getWidth() / 2;
        long height = Node.getHeight() / 2;
        if ((e.getX() < graphDimension.getWidth()) && (e.getY() < graphDimension.getHeight()) && (e.getX() + 2 * width > visibleDimension.getWidth() + controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue()) && (e.getY() + 2 * height > visibleDimension.getHeight() + controller.getScreen().getJspGraph().getVerticalScrollBar().getValue())) {
            controller.getScreen().getJspGraph().getHorizontalScrollBar().setValue((int) (e.getX() + 2 * width - visibleDimension.getWidth()));
            controller.getScreen().getJspGraph().getVerticalScrollBar().setValue((int) (e.getY() + 2 * height - visibleDimension.getHeight()));
        } else if ((e.getX() < graphDimension.getWidth()) && (e.getX() + 2 * width > visibleDimension.getWidth() + controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue()) && (e.getY() + 2 * height <= visibleDimension.getHeight() + controller.getScreen().getJspGraph().getVerticalScrollBar().getValue())) {
            controller.getScreen().getJspGraph().getHorizontalScrollBar().setValue((int) (e.getX() + 2 * width - visibleDimension.getWidth()));
        } else if ((e.getY() < graphDimension.getHeight()) && (e.getX() + 2 * width <= visibleDimension.getWidth() + controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue()) && (e.getY() + 2 * height > visibleDimension.getHeight() + controller.getScreen().getJspGraph().getVerticalScrollBar().getValue())) {
            controller.getScreen().getJspGraph().getVerticalScrollBar().setValue((int) (e.getY() + 2 * height - visibleDimension.getHeight()));
        } else if ((e.getX() > 0) && (e.getY() > 0) && (e.getX() - width < controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue()) && (e.getY() - height < controller.getScreen().getJspGraph().getVerticalScrollBar().getValue())) {
            controller.getScreen().getJspGraph().getHorizontalScrollBar().setValue((int) (e.getX() - width));
            controller.getScreen().getJspGraph().getVerticalScrollBar().setValue((int) (e.getY() - height));
        } else if ((e.getY() > 0) && (e.getX() - width >= controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue()) && (e.getY() - height < controller.getScreen().getJspGraph().getVerticalScrollBar().getValue())) {
            controller.getScreen().getJspGraph().getVerticalScrollBar().setValue((int) (e.getY() - height));
        } else if ((e.getX() > 0) && (e.getX() - width < controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue()) && (e.getY() - height >= controller.getScreen().getJspGraph().getVerticalScrollBar().getValue())) {
            controller.getScreen().getJspGraph().getHorizontalScrollBar().setValue((int) (e.getX() - width));
        }
        if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
            int compareX = 0;
            int compareY = 0;
            int x = -1;
            int y = -1;
            if (bMoveNode) {
                compareX = (int) width;
                compareY = (int) height;
            }
            if ((e.getX() > compareX) && (e.getY() > compareY) && (e.getX() < graphDimension.getWidth() - compareX) && (e.getY() < graphDimension.getHeight() - compareY)) {
                x = e.getX();
                y = e.getY();
            } else if ((e.getX() <= compareX) && (e.getY() > compareY) && (e.getX() < graphDimension.getWidth() - compareX) && (e.getY() < graphDimension.getHeight() - compareY)) {
                x = compareX;
                y = e.getY();
            } else if ((e.getX() > compareX) && (e.getY() <= compareY) && (e.getX() < graphDimension.getWidth() - compareX) && (e.getY() < graphDimension.getHeight() - compareY)) {
                x = e.getX();
                y = compareY;
            } else if ((e.getX() <= compareX) && (e.getY() <= compareY) && (e.getX() < graphDimension.getWidth() - compareX) && (e.getY() < graphDimension.getHeight() - compareY)) {
                x = compareX;
                y = compareY;
            } else if ((e.getX() <= compareX) && (e.getY() > compareY) && (e.getX() < graphDimension.getWidth() - compareX) && (e.getY() >= graphDimension.getHeight() - compareY)) {
                x = compareX;
                y = (int) graphDimension.getHeight() - compareY;
            } else if ((e.getX() > compareX) && (e.getY() > compareY) && (e.getX() < graphDimension.getWidth() - compareX) && (e.getY() >= graphDimension.getHeight() - compareY)) {
                x = e.getX();
                y = (int) graphDimension.getHeight() - compareY;
            } else if ((e.getX() > compareX) && (e.getY() > compareY) && (e.getX() >= graphDimension.getWidth() - compareX) && (e.getY() >= graphDimension.getHeight() - compareY)) {
                x = (int) graphDimension.getWidth() - compareX;
                y = (int) graphDimension.getHeight() - compareY;
            } else if ((e.getX() > compareX) && (e.getY() > compareY) && (e.getX() >= graphDimension.getWidth() - compareX) && (e.getY() < graphDimension.getHeight() - compareY)) {
                x = (int) graphDimension.getWidth() - compareX;
                y = e.getY();
            } else if ((e.getX() > compareX) && (e.getY() <= compareY) && (e.getX() >= graphDimension.getWidth() - compareX) && (e.getY() < graphDimension.getHeight() - compareY)) {
                x = (int) graphDimension.getWidth() - compareX;
                y = compareY;
            }
            if (x != -1 && y != -1) {
                if (getAction() == GraphAction.CREATE_EDGE && bMoveEdge) {
                    updateMovingEdge(x, y);
                } else if (bMoveNode) {
                    updateMovingNode(x, y);
                }
            }
        }
    }

    /**
	 *  M�todo respons�vel por tratar o evento de mover o mouse
	 *
	 *@param  e  O <code>MouseEvent</code>
	 *@see MouseEvent
	 */
    public void mouseMoved(MouseEvent e) {
    }

    public void selectObject(IDrawable object) {
        unselectAll();
        object.setSelected(true);
        selected = object;
    }

    /**
	 *  M�todo respons�vel por atualizar o arco (<code>Edge</code>) atual ao se mover um arco
	 *
	 *@param  x  Posi��o x (double) da ponta do arco
	 *@param  y  Posi��o y (double) da ponta do arco
	 *@see Edge
	 */
    public void updateMovingEdge(double x, double y) {
        movingEdge.setDestinationPosition(x, y);
        update();
    }

    /**
	 *  M�todo respons�vel por atualizar o n� (<code>Node</code>) atual ao se mover um n�
	 *
	 *@param  x  Posi��o x (double) do centro do n�
	 *@param  y  Posi��o y (double) do centro do n�
	 *@see Node
	 */
    public void updateMovingNode(double x, double y) {
        movingNode.setPosition(x, y);
        update();
    }

    /**
	 *  M�todo respons�vel por atualizar o ponto final de sele��o ao se mover o mouse para sele��o
	 *
	 *@param  x  Posi��o x (double)
	 *@param  y  Posi��o y (double)
	 */
    public void updateEndSelectionPoint(double x, double y) {
        endSelectionPoint.setLocation(x, y);
        update();
    }

    public void insertEdge(Edge edge) {
        try {
            controller.insertEdge(edge);
        } catch (MEBNConstructionException me) {
            JOptionPane.showMessageDialog(controller.getScreen().getMebnEditionPane(), me.getMessage(), "MEBN Construction Error", JOptionPane.ERROR_MESSAGE);
        } catch (CycleFoundException cycle) {
            JOptionPane.showMessageDialog(controller.getScreen().getMebnEditionPane(), cycle.getMessage(), "Cycle Found Exception", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edge.setNew(false);
        update();
    }

    /**
	 *  M�todo respons�vel por definir quais os obejos (<code>Node</code> e/ou <code>Edge</code>) foram selecionados
	 *
	 *@param  p1  Ponto p1 (Point2D.Double) do in�cio do ret�ngulo
	 *@param  p2  Ponto p2 (Point2D.Double) do fim do ret�ngulo
	 *@see Node
	 *@see Point2D.Double
	 */
    public void setSelectedGroup(Point2D.Double p1, Point2D.Double p2) {
        unselectAll();
        for (int i = 0; i < nodeList.size(); i++) {
            Node nodeAux = (Node) nodeList.get(i);
            if ((((p1.getX() <= p2.getX()) && (nodeAux.getPosition().getX() >= p1.getX()) && (nodeAux.getPosition().getX() <= p2.getX())) || ((p2.getX() < p1.getX()) && (nodeAux.getPosition().getX() >= p2.getX()) && (nodeAux.getPosition().getX() <= p1.getX()))) && (((p1.getY() <= p2.getY()) && (nodeAux.getPosition().getY() >= p1.getY()) && (nodeAux.getPosition().getY() <= p2.getY())) || ((p2.getY() < p1.getY()) && (nodeAux.getPosition().getY() >= p2.getY()) && (nodeAux.getPosition().getY() <= p1.getY())))) {
                selectedGroup.add(nodeAux);
                nodeAux.setSelected(true);
            }
        }
        for (int i = 0; i < edgeList.size(); i++) {
            Edge arcAux = (Edge) edgeList.get(i);
            Node nodeAux2 = arcAux.getDestinationNode();
            if (((((p1.getX() <= p2.getX()) && (nodeAux2.getPosition().getX() >= p1.getX()) && (nodeAux2.getPosition().getX() <= p2.getX())) || ((p2.getX() < p1.getX()) && (nodeAux2.getPosition().getX() >= p2.getX()) && (nodeAux2.getPosition().getX() <= p1.getX()))) && (((p1.getY() <= p2.getY()) && (nodeAux2.getPosition().getY() >= p1.getY()) && (nodeAux2.getPosition().getY() <= p2.getY())) || ((p2.getY() < p1.getY()) && (nodeAux2.getPosition().getY() >= p2.getY()) && (nodeAux2.getPosition().getY() <= p1.getY()))))) {
                selectedGroup.add(arcAux);
                arcAux.setSelected(true);
            }
        }
    }

    /**
	 * Unselect all objects selected, from single and group selection.  
	 *
	 */
    public void unselectAll() {
        if (selected != null) {
            selected.setSelected(false);
            selected = null;
        }
        for (int i = 0; i < selectedGroup.size(); i++) {
            selectedGroup.get(i).setSelected(false);
        }
        selectedGroup.clear();
    }

    /**
	 * Set the action to be taken.
	 * @param action The action to be taken.
	 */
    public void setAction(GraphAction action) {
        switch(action) {
            case CREATE_PROBABILISTIC_NODE:
            case CREATE_DECISION_NODE:
            case CREATE_UTILITY_NODE:
            case CREATE_CONTEXT_NODE:
            case CREATE_INPUT_NODE:
            case CREATE_RESIDENT_NODE:
            case CREATE_ORDINARYVARIABLE_NODE:
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                break;
            case CREATE_EDGE:
            case SELECT_MANY_OBJECTS:
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                break;
            default:
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
        }
        this.action = action;
    }

    /**
	 * Get the action to be taken.
	 * @return The action to be taken.
	 */
    public GraphAction getAction() {
        return action;
    }

    public void resetGraph() {
        edgeList = controller.getGraph().getEdges();
        nodeList = controller.getGraph().getNodes();
        selectedGroup = new ArrayList<IDrawable>();
        startSelectionPoint = new Point2D.Double();
        endSelectionPoint = new Point2D.Double();
        drawSelection = new DrawDashRectangle(startSelectionPoint, endSelectionPoint);
        bMoveEdge = false;
        bMoveNode = false;
        graphDimension = new Dimension(1500, 1500);
        visibleDimension = new Dimension(0, 0);
        action = GraphAction.NONE;
        JMenuItem item = new JMenuItem(resource.getString("properties"));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                controller.showExplanationProperties((ProbabilisticNode) getSelected());
            }
        });
        popup.add(item);
        repaint();
    }

    /**
	 * Reset the graph to a empty graph. 
	 */
    public void showEmptyGraph() {
        edgeList = new ArrayList<Edge>();
        nodeList = new NodeList();
        selectedGroup = new ArrayList<IDrawable>();
        startSelectionPoint = new Point2D.Double();
        endSelectionPoint = new Point2D.Double();
        drawSelection = new DrawDashRectangle(startSelectionPoint, endSelectionPoint);
        bMoveEdge = false;
        bMoveNode = false;
        graphDimension = new Dimension(1500, 1500);
        visibleDimension = new Dimension(0, 0);
        action = GraphAction.NONE;
        repaint();
    }

    /**
	 *  M�todo respons�vel por pintar a rede Bayesiana, ou seja, o objeto da classe GraphPane
	 *
	 *@param  g  O <code>Graphics</code>
	 *@see Graphics
	 */
    public void paint(Graphics g) {
        graphBoard = (Graphics2D) g;
        graphBoard.setBackground(getBackgroundColor());
        graphBoard.clearRect((int) controller.getScreen().getJspGraph().getHorizontalScrollBar().getValue(), (int) controller.getScreen().getJspGraph().getVerticalScrollBar().getValue(), (int) (controller.getScreen().getJspGraph().getSize().getWidth()), (int) (controller.getScreen().getJspGraph().getSize().getHeight()));
        graphBoard.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getAction() == GraphAction.SELECT_MANY_OBJECTS) {
            drawSelection.paint(graphBoard);
        }
        graphBoard.setStroke(new BasicStroke(1));
        for (int i = 0; i < nodeList.size(); i++) {
            nodeList.get(i).paint(graphBoard);
        }
        if (bMoveEdge) {
            movingEdge.paint(graphBoard);
        }
        for (Edge edge : edgeList) {
            edge.paint(graphBoard);
        }
    }
}
