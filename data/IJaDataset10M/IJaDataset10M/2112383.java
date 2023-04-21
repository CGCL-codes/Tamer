package org.jazzteam.geom.frames;

import org.jazzteam.geom.ShapesRunnable;
import org.jazzteam.geom.actions.*;
import org.jazzteam.geom.draws.ShapePainter;
import org.jazzteam.geom.shapes.AbstractShape;
import sun.awt.VerticalBagLayout;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Класс создающий все компоненты на игровом поле, включая кнопки/панели и т.д.
 *
 * @author: Andrey Avtuchovich
 * @date: 19.03.12 17:18
 */
public class Playground extends JFrame {

    private static final int WIDTH_FRAME = 700;

    private static final int HIGHT_FRAME = 500;

    private static ShapePainter shapePainter;

    public static JSlider sliderSpeed;

    public static JLabel countShapes;

    public static JButton addRectangleButton;

    public static JButton startButton;

    public static JButton clearButton;

    public static JButton saveButton;

    public static JButton addCircleButton;

    public static JComboBox caseMove;

    private static Thread t;

    private static Runnable r;

    public Playground() {
        shapePainter = new ShapePainter();
        initCountShapes();
        initSliderSpeed();
        initCaseMove();
        shapePainter.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        this.setSize(WIDTH_FRAME, HIGHT_FRAME);
        this.setLocationRelativeTo(null);
        add(shapePainter, BorderLayout.CENTER);
        addRectangleButton = new JButton("Add rectangles");
        addCircleButton = new JButton("Add circles");
        saveButton = new JButton("Save coordinates");
        startButton = new JButton("Start");
        clearButton = new JButton("Clear playground");
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.X_AXIS));
        buttonpanel.add(startButton);
        startButton.addActionListener(new StartButtonListener());
        buttonpanel.add(addRectangleButton);
        addRectangleButton.setVisible(false);
        addRectangleButton.addActionListener(new AddRectangleListener());
        buttonpanel.add(addCircleButton);
        addCircleButton.setVisible(false);
        addCircleButton.addActionListener(new AddCircleListener());
        buttonpanel.add(saveButton);
        saveButton.addActionListener(new SaveCoordinatesListener());
        buttonpanel.add(clearButton);
        clearButton.addActionListener(new ClearPlaygroundListener());
        add(buttonpanel, BorderLayout.SOUTH);
        JPanel settingPlayground = new JPanel();
        settingPlayground.setLayout(new VerticalBagLayout());
        settingPlayground.add(countShapes);
        settingPlayground.add(sliderSpeed);
        settingPlayground.add(caseMove);
        add(settingPlayground, BorderLayout.EAST);
    }

    public void initCountShapes() {
        countShapes = new JLabel();
        countShapes.setText("Count shapes into playground: 0");
        countShapes.setPreferredSize(new Dimension(100, 50));
    }

    public void initCaseMove() {
        String[] items = { "Chaotic move", "Sinusoid move", "Circle move" };
        caseMove = new JComboBox(items);
        caseMove.setSize(new Dimension(100, 30));
    }

    public void initSliderSpeed() {
        sliderSpeed = new JSlider();
        sliderSpeed.setMinimum(0);
        sliderSpeed.setMaximum(100);
        sliderSpeed.setValue(10);
        sliderSpeed.setPreferredSize(new Dimension(300, 100));
        sliderSpeed.setBorder(BorderFactory.createTitledBorder("Speed motions"));
        sliderSpeed.setMajorTickSpacing(10);
        sliderSpeed.setMinorTickSpacing(2);
        sliderSpeed.setPaintTicks(true);
        sliderSpeed.setPaintLabels(true);
        sliderSpeed.addChangeListener(new ChangeSpeedListener());
    }

    /**
     * Получаем рандомный цвет
     *
     * @return полученный random цвет
     */
    public static Color getRandomColor() {
        Random rand = new Random();
        return (new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
    }

    /**
     * Запуск потока с фигурами
     *
     * @param shapes ArrayList содержащий фигуры
     */
    public static void startPlayground(ArrayList<AbstractShape> shapes) {
        r = new ShapesRunnable(shapes, shapePainter);
        t = new Thread(r);
        t.start();
    }
}
