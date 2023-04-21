package org.field.comp.object;

import javax.media.opengl.GL;
import org.field.comp.geometry.Point3D;
import org.field.ui.controls.WView;
import org.field.util.Project;
import org.field.util.Util;
import org.w3c.dom.Element;
import com.trolltech.qt.gui.QDoubleValidator;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

public class ObjectPoint extends ObjectBasic {

    public Point3D point = new Point3D();

    public double charge = 0;

    public ObjectPoint() {
        this(new Point3D(), "new point", 0);
    }

    public ObjectPoint(Point3D point, String name, double charge) {
        super(ObjectType.Point);
        this.point = point;
        this.name = name;
        this.charge = charge;
    }

    @Override
    public void readFromXML(Element element) {
        name = element.getAttribute("name");
        point = new Point3D(Double.parseDouble(element.getAttribute("px")), Double.parseDouble(element.getAttribute("py")), Double.parseDouble(element.getAttribute("pz")));
        charge = Double.parseDouble(element.getAttribute("charge"));
    }

    @Override
    public void writeToXML(Element element) {
        element.setAttribute("type", "point");
        element.setAttribute("name", name);
        element.setAttribute("px", Double.toString(point.x));
        element.setAttribute("py", Double.toString(point.y));
        element.setAttribute("pz", Double.toString(point.z));
        element.setAttribute("charge", Double.toString(charge));
    }

    @Override
    public Point3D getResultE(Point3D p) {
        return getResultD(p).mult(1 / Util.EPS0);
    }

    @Override
    public Point3D getResultD(Point3D p) {
        Point3D D = new Point3D();
        Point3D rp = new Point3D(point.x - p.x, point.y - p.y, point.z - p.z);
        double rpLength3 = Math.pow(Util.vectorLength(rp), 2);
        D.x = charge * rp.x / (4 * Math.PI * rpLength3);
        D.y = charge * rp.y / (4 * Math.PI * rpLength3);
        D.z = charge * rp.z / (4 * Math.PI * rpLength3);
        return D;
    }

    @Override
    public Point3D getResultB(Point3D p) {
        return new Point3D();
    }

    @Override
    public Point3D getResultH(Point3D p) {
        return new Point3D();
    }

    @Override
    public void drawGL(WView view) {
        view.gl.glBegin(GL.GL_POINT);
        view.qglColor(view.scene.getSettings().colorObject);
        if (view.scene.runtimeSettings.objectSelected != null) if (view.scene.runtimeSettings.objectSelected.equals(this)) view.qglColor(view.scene.getSettings().colorObjectSelected);
        view.gl.glPushMatrix();
        view.gl.glTranslated(point.x, point.y, point.z);
        view.glu.gluSphere(view.quadric, 0.003, 12, 12);
        view.gl.glPopMatrix();
        view.gl.glEnd();
    }

    @Override
    public QIcon getIcon() {
        return Project.getIcon("object_point");
    }

    @Override
    public int showDialog(QWidget parent) {
        DObjectPoint objectWireWidget = new DObjectPoint(parent);
        return objectWireWidget.exec();
    }

    class DObjectPoint extends DObjectBasic {

        QLineEdit txtPointX = new QLineEdit("0");

        QLineEdit txtPointY = new QLineEdit("0");

        QLineEdit txtPointZ = new QLineEdit("0");

        QLineEdit txtCharge = new QLineEdit("0");

        public DObjectPoint(QWidget parent) {
            super(parent);
            setMinimumSize(300, 200);
            setWindowTitle("Point");
            createDialog();
            load();
        }

        @Override
        protected QLayout createContent() {
            txtPointX.setValidator(new QDoubleValidator(txtPointX));
            txtPointY.setValidator(new QDoubleValidator(txtPointY));
            txtPointZ.setValidator(new QDoubleValidator(txtPointZ));
            txtCharge.setValidator(new QDoubleValidator(txtCharge));
            QGridLayout layoutPoint = new QGridLayout();
            layoutPoint.addWidget(new QLabel("x (m):"), 0, 0);
            layoutPoint.addWidget(new QLabel("y (m):"), 1, 0);
            layoutPoint.addWidget(new QLabel("z (m):"), 2, 0);
            layoutPoint.addWidget(txtPointX, 0, 1);
            layoutPoint.addWidget(txtPointY, 1, 1);
            layoutPoint.addWidget(txtPointZ, 2, 1);
            QGroupBox grpPoint = new QGroupBox(tr("Point"));
            grpPoint.setLayout(layoutPoint);
            QGridLayout layoutVariables = new QGridLayout();
            layoutVariables.addWidget(new QLabel("Charge <i>Q</i> (C):"), 1, 0);
            layoutVariables.addWidget(txtCharge, 1, 1);
            QGroupBox grpVariables = new QGroupBox(tr("Variables"));
            grpVariables.setLayout(layoutVariables);
            QVBoxLayout layoutContent = new QVBoxLayout();
            layoutContent.addWidget(grpPoint);
            layoutContent.addWidget(grpVariables);
            return layoutContent;
        }

        @Override
        protected void load() {
            super.load();
            txtPointX.setText(Double.toString(point.x));
            txtPointY.setText(Double.toString(point.y));
            txtPointZ.setText(Double.toString(point.z));
            txtCharge.setText(Double.toString(charge));
        }

        @Override
        protected void save() {
            super.save();
            point.x = Double.parseDouble(txtPointX.text());
            point.y = Double.parseDouble(txtPointY.text());
            point.z = Double.parseDouble(txtPointZ.text());
            charge = Double.parseDouble(txtCharge.text());
        }
    }

    @Override
    public void move(Point3D p) {
        point = point.add(p);
    }

    @Override
    public void rotate(RotationAxis axis, double angle) {
        return;
    }

    @Override
    public void scale(Point3D p) {
        return;
    }
}
