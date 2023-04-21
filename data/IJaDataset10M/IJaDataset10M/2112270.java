package ConfigFases;

import GUI.TelaJogo;
import classes.Recursos;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * Created on : Feb 9, 2012, 8:21:49 PM
 * @author diogo
 */
public class FaseNumeros extends Fase {

    private static final int iStartPosX = 170;

    private static final int iDistanceX = 60;

    private static final int iFigurePosY = 80;

    private static final int iObjectPosY = 160;

    public static final String caminhoImg = "numeros/";

    public static final String FIGURE_NAME = "_desenho";

    public static final String OBJECT_NAME = "_figura";

    public static final String NUMBER_NAME = "_numero";

    public static final String BIG_IMAGE_SUFIX = "_grande";

    /**
	 * gets an image for this game.
	 * @param sImageName
	 * @return
	 */
    public static Icon getImage(String sImageName) {
        Icon a = Recursos.GetInsance().getImagem(caminhoImg + sImageName + ".jpg");
        return a;
    }

    private JLabel basicFigure;

    private JLabel objectView;

    public FaseNumeros(String name) {
        super(name);
    }

    public void InitComponents(FaseControladorBase controller, java.awt.event.MouseAdapter listener) {
        String sName;
        basicFigure = new JLabel();
        sName = GetName() + FIGURE_NAME;
        basicFigure.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        basicFigure.setIcon(getImage(sName));
        basicFigure.addMouseListener(listener);
        controller.GetTela().add(basicFigure);
        basicFigure.setSize(60, 60);
        basicFigure.setName(sName);
        sName = GetName() + OBJECT_NAME;
        objectView = new JLabel();
        objectView.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectView.setIcon(getImage(sName));
        objectView.addMouseListener(listener);
        controller.GetTela().add(objectView);
        objectView.setSize(60, 60);
        objectView.setName(sName);
    }

    /**
	 * sets the position the objectView based on a position
	 * @param iPosition starts from 0
	 */
    public void setFigurePosition(int iPosition) {
        basicFigure.setLocation(getPositionX(iPosition), iFigurePosY);
    }

    /**
	 * sets the position the objectView based on a position
	 * @param iPosition starts from 0
	 */
    public void setObjectPosition(int iPosition) {
        objectView.setLocation(getPositionX(iPosition), iObjectPosY);
    }

    /**
	 * calculates the X position for a UI item
	 * @param iPosition starts from 0
	 * @return
	 */
    private int getPositionX(int iPosition) {
        return iStartPosX + iPosition * iDistanceX;
    }
}
