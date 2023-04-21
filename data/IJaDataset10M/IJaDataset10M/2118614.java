package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;
import javax.swing.JComponent;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import model.*;
import util.*;
import view.interfaces.IView;

public class CityView extends JComponent implements IView {

    protected City city;

    protected List views;

    Image backgroundImage;

    int width, height;

    /**
	 * @param viewFile
	 * @param city
	 */
    public CityView(String viewFile, City city) {
        super();
        this.city = city;
        views = new LinkedList();
        loadView(viewFile);
        initialize();
        city.setViewObject(this);
    }

    public void initialize() {
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(width, height));
    }

    private void loadView(String viewFile) {
        try {
            SAXReader reader = new SAXReader();
            reader.setValidation(false);
            Document document = reader.read(viewFile);
            Element rootElement = document.getRootElement();
            Element skinNode = rootElement.element("Skin");
            Element positionsNode = rootElement.element("Positions");
            Element s = skinNode.element("City");
            if (s != null) {
                width = Integer.parseInt(s.attributeValue("width"));
                height = Integer.parseInt(s.attributeValue("height"));
                this.setSize(width, height);
                backgroundImage = ImageLoader.loadImage(s.attributeValue("backgroundImage"));
            }
            s = skinNode.element("Field");
            Image fieldImage = null;
            int fieldWidth = 0, fieldHeight = 0;
            if (s != null) {
                fieldWidth = Integer.parseInt(s.attributeValue("width"));
                fieldHeight = Integer.parseInt(s.attributeValue("height"));
                fieldImage = ImageLoader.loadImage(s.attributeValue("image"));
            }
            Hashtable images = new Hashtable();
            for (Iterator sit = skinNode.elementIterator(); sit.hasNext(); ) {
                Element sub = (Element) sit.next();
                if (!(sub.getName().equals("City") || sub.getName().equals("Field"))) {
                    images.put(sub.getName(), ImageLoader.loadImage(sub.attributeValue("image")));
                }
            }
            for (Iterator fit = positionsNode.element("Fields").elementIterator("Field"); fit.hasNext(); ) {
                Element f = (Element) fit.next();
                int id = Integer.parseInt(f.attributeValue("id"));
                Field field = city.getFieldByID(id);
                if (field != null) {
                    int x = Integer.parseInt(f.attributeValue("x"));
                    int y = Integer.parseInt(f.attributeValue("y"));
                    Image fImage = ImageLoader.loadImage(f.attributeValue("uniqueImage"));
                    if (fImage == null) {
                        fImage = fieldImage;
                    }
                    FieldView fv = new FieldView(field, x, y, fieldWidth, fieldHeight, fImage);
                    views.add(fv);
                }
            }
            if (positionsNode.element("Subjects") != null) {
                for (Iterator sit = positionsNode.element("Subjects").elementIterator(); sit.hasNext(); ) {
                    Element sNode = (Element) sit.next();
                    int id = Integer.parseInt(sNode.attributeValue("id"));
                    String sType = sNode.getName();
                    Subject subject = city.getSubjectByID(id, sType);
                    if (subject != null) {
                        Image sImage = ImageLoader.loadImage(sNode.attributeValue("uniqueImage"));
                        if (sImage == null) {
                            sImage = (Image) images.get(sType);
                        }
                        views.add(viewObjectFactory(subject, sImage));
                    }
                }
            }
            for (Iterator sit = city.getSubjectIterator(); sit.hasNext(); ) {
                Subject subject = (Subject) sit.next();
                if (subject.getViewObject() == null) {
                    Image sImage = (Image) images.get(Classes.getSimpleName(subject));
                    views.add(viewObjectFactory(subject, sImage));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private ViewObject viewObjectFactory(Subject subject, Image sImage) {
        if (subject == null) {
            return null;
        }
        if (subject.getViewObject() == null) {
            String sType = Classes.getSimpleName(subject);
            if (sType.equals("Teacher")) {
                return new TeacherView((Teacher) subject, sImage);
            } else if (sType.equals("Girl")) {
                return new GirlView((Girl) subject, sImage);
            } else if (sType.equals("Boy")) {
                return new BoyView((Boy) subject, sImage);
            } else if (sType.equals("Dog")) {
                return new DogView((Dog) subject, sImage);
            } else if (sType.equals("ChocolateAutomat")) {
                return new ChocolateAutomatView((ChocolateAutomat) subject, sImage);
            } else if (sType.equals("ToyShop")) {
                return new ToyShopView((ToyShop) subject, sImage);
            } else if (sType.equals("KinderGarten")) {
                return new KinderGartenView((KinderGarten) subject, sImage);
            }
        }
        return null;
    }

    /**
	 * @param view
	 */
    public void addViewObject(IView view) {
    }

    Thread refreshThread = null;

    public void modelModified() {
        if (refreshThread == null) {
            refreshThread = new Thread() {

                public void run() {
                    try {
                        while (true) {
                            repaint();
                            sleep(30);
                        }
                    } catch (Exception e) {
                        refreshThread = null;
                    }
                }
            };
            refreshThread.start();
        }
    }

    public void draw(Graphics g) {
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(backgroundImage, 0, 0, width, height, this);
        Iterator it = views.iterator();
        while (it.hasNext()) {
            ((IView) it.next()).draw(g);
        }
    }
}
