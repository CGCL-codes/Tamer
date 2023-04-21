package lpm_gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.*;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * 
 * Class with the components of the upsite bar.
 * 
 * @author Enrique Garca Orive
 *
 */
public class UpButtons extends JPanel implements ActionListener, KeyListener {

    LightPointMatrix lpmMatrix;

    JFrame mainJFrame;

    JButton clear = new JButton("CLEAR");

    JButton markall = new JButton("MARK ALL");

    JButton makemtx = new JButton("MAKE MATRIX");

    JLabel inputLetterLabel;

    JLabel inputLetterSizeLabel;

    JFormattedTextField inputLetterField;

    JFormattedTextField inputLetterSizeField;

    int lado_vert;

    int lado_horiz;

    int letterSize;

    double[][] actualMatrix;

    private static Logger logger;

    public UpButtons(JFrame f, JPanel mainJPanel, SaveMatrixActions saveMatrixActions) {
        logger = Logger.getLogger(this.getClass());
        ConsoleAppender appender = new ConsoleAppender(new PatternLayout());
        logger.addAppender(appender);
        mainJFrame = f;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
        this.setBackground(new Color(230, 255, 255));
        this.setOpaque(true);
        inputLetterLabel = new JLabel("Letter: ");
        inputLetterField = new JFormattedTextField();
        inputLetterField.setValue("");
        inputLetterField.setColumns(2);
        inputLetterLabel.setLabelFor(inputLetterField);
        inputLetterField.setActionCommand("inputLetter");
        inputLetterField.addActionListener(this);
        inputLetterField.addKeyListener(this);
        this.add(inputLetterLabel);
        this.add(inputLetterField);
        inputLetterSizeLabel = new JLabel("Letter size: ");
        inputLetterSizeField = new JFormattedTextField();
        inputLetterSizeField.setValue("");
        inputLetterSizeField.setColumns(2);
        inputLetterSizeLabel.setLabelFor(inputLetterSizeField);
        inputLetterSizeField.setActionCommand("inputLetterSize");
        inputLetterSizeField.addActionListener(this);
        this.add(inputLetterSizeLabel);
        this.add(inputLetterSizeField);
        clear.setMargin(new Insets(1, 2, 1, 2));
        clear.setActionCommand("clear");
        clear.addActionListener(this);
        this.add(clear);
        markall.setMargin(new Insets(1, 2, 1, 2));
        markall.setActionCommand("markall");
        markall.addActionListener(this);
        this.add(markall);
        makemtx.setMargin(new Insets(1, 2, 1, 2));
        makemtx.setActionCommand("Save Matrix");
        makemtx.addActionListener(saveMatrixActions);
        this.add(makemtx);
    }

    public void actionPerformed(ActionEvent evt) {
        lpmMatrix = (LightPointMatrix) ((JPanel) mainJFrame.getContentPane()).getComponent(0);
        if (evt.getActionCommand().equals("clear")) lpmMatrix.ClearMatrix();
        if (evt.getActionCommand().equals("markall")) {
            lpmMatrix.MarkAll();
        }
        if (evt.getActionCommand().equals("inputLetterSize")) {
            if (inputLetterSizeField.getValue() == null) logger.error("Valor tomado de inputLetterSizeField es null");
            logger.info(inputLetterSizeField.getValue().toString());
            lpmMatrix.setLetterSize(Integer.parseInt((inputLetterSizeField.getValue()).toString()));
        }
    }

    public void keyPressed(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            String letra = (inputLetterField.getValue()).toString();
            if (letra.length() > 0) {
                lpmMatrix.ClearMatrix();
                switch(letra.charAt(0)) {
                    case 'c':
                        lpmMatrix.topSide();
                        lpmMatrix.leftSide();
                        lpmMatrix.downSide();
                        break;
                    case 'C':
                        lpmMatrix.topSide();
                        lpmMatrix.leftSide();
                        lpmMatrix.downSide();
                        break;
                    case 'L':
                        lpmMatrix.leftSide();
                        lpmMatrix.downSide();
                        break;
                    case 'l':
                        lpmMatrix.leftSide();
                        lpmMatrix.downSide();
                        break;
                    case 'n':
                        lpmMatrix.leftSide();
                        lpmMatrix.diago1Letter();
                        lpmMatrix.rightSide();
                        break;
                    case 'N':
                        lpmMatrix.leftSide();
                        lpmMatrix.diago1Letter();
                        lpmMatrix.rightSide();
                        break;
                    case 't':
                        lpmMatrix.topSide();
                        lpmMatrix.vertLetter();
                        break;
                    case 'T':
                        lpmMatrix.topSide();
                        lpmMatrix.vertLetter();
                        break;
                    case 'u':
                        lpmMatrix.leftSide();
                        lpmMatrix.rightSide();
                        lpmMatrix.downSide();
                        break;
                    case 'U':
                        lpmMatrix.leftSide();
                        lpmMatrix.rightSide();
                        lpmMatrix.downSide();
                        break;
                    case 'x':
                        lpmMatrix.diago1Letter();
                        lpmMatrix.diago2Letter();
                        break;
                    case 'X':
                        lpmMatrix.diago1Letter();
                        lpmMatrix.diago2Letter();
                        break;
                    case 'z':
                        lpmMatrix.topSide();
                        lpmMatrix.diago2Letter();
                        lpmMatrix.downSide();
                        break;
                    case 'Z':
                        lpmMatrix.topSide();
                        lpmMatrix.diago2Letter();
                        lpmMatrix.downSide();
                        break;
                    default:
                        logger.info("aun no est implementada");
                        break;
                }
            }
        }
    }

    public void keyTyped(KeyEvent event) {
    }
}
