package schwimmbad.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import schwimmbad.gui.SchwimmbadGUI;

public class Main extends JApplet {

    SchwimmbadGUI schwimmbad_gui;

    public static Schwimmbad schwimmbad;

    static Statistics statistics;

    public static Color bgcolor;

    boolean simulation_running;

    int slider_value;

    JButton b_start_stop_simulation;

    JSlider sl_time;

    JButton b_add_new_swimmer;

    JButton b_add_new_swimmer_rand;

    JLabel Lsimulation_speed;

    JButton Bincrease_speed;

    JButton Bdecrease_speed;

    JCheckBox cb_draw_positions;

    static final int frame_rate = 10;

    public static double simulation_rate = 1;

    long start_memory;

    public Main() {
        schwimmbad_gui = new SchwimmbadGUI();
        schwimmbad = new Schwimmbad();
        statistics = new Statistics();
        init();
        simulation_running = false;
        Schwimmer danzi = new Schwimmer();
        start_memory = Runtime.getRuntime().freeMemory();
        startSimulation();
        bgcolor = this.getBackground();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    private void startSimulation() {
        new Thread() {

            public void run() {
                int time_sleept = 0;
                while (true) {
                    int must_sleep_time = Math.max(1, (int) (1000. / frame_rate / simulation_rate));
                    try {
                        Thread.sleep(Math.max(1, must_sleep_time - time_sleept));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long start = System.currentTimeMillis();
                    if (simulation_running) {
                        schwimmbad.tick();
                    }
                    if (!statistics.paused) {
                        statistics.simulation_time++;
                    }
                    boolean draw_schwimmer = simulation_running;
                    if (true) {
                        try {
                            if (draw_schwimmer) schwimmbad_gui.drawSchwimmer(schwimmbad.getSchwimmer());
                        } catch (ConcurrentModificationException e) {
                        }
                        Graphics g = Main.this.getGraphics();
                        g.translate(15, 15);
                        if (!draw_schwimmer) statistics.paintPositionUH(g);
                        g.translate(-15, -15);
                        g.translate(420, 350);
                        statistics.paintUnHappinessGraph(g);
                    }
                    time_sleept = (int) (System.currentTimeMillis() - start);
                }
            }

            ;
        }.start();
    }

    @Override
    public void init() {
        super.init();
        this.setSize(800, 600);
        this.setBounds(0, 0, 800, 600);
        setLayout(null);
        add(schwimmbad_gui);
        setVisible(true);
        schwimmbad_gui.paintSchwimmbad();
        b_start_stop_simulation = new JButton("start");
        b_start_stop_simulation.setBounds(440, 15, 100, 30);
        b_start_stop_simulation.addActionListener(onStartStopButton());
        this.add(b_start_stop_simulation);
        sl_time = new JSlider(40, 80, 55);
        slider_value = sl_time.getValue();
        sl_time.setMajorTickSpacing(20);
        sl_time.setMinorTickSpacing(5);
        sl_time.setPaintLabels(true);
        sl_time.setPaintTicks(true);
        sl_time.addChangeListener(onSlideChange());
        sl_time.setBounds(440, 60, 100, 30);
        this.add(sl_time);
        b_add_new_swimmer = new JButton("add");
        b_add_new_swimmer.setBounds(440, 90, 100, 30);
        b_add_new_swimmer.addActionListener(onAddSwimmerButton());
        this.add(b_add_new_swimmer);
        b_add_new_swimmer_rand = new JButton("rand");
        b_add_new_swimmer_rand.setBounds(540, 90, 100, 30);
        b_add_new_swimmer_rand.addActionListener(onAddSwimmerRandButton());
        this.add(b_add_new_swimmer_rand);
        Lsimulation_speed = new JLabel("1x");
        Lsimulation_speed.setBounds(480, 120, 20, 20);
        this.add(Lsimulation_speed);
        Bdecrease_speed = new JButton("-");
        Bdecrease_speed.setBounds(440, 120, 30, 20);
        Bdecrease_speed.addActionListener(onDecreaseSpeedButton(Lsimulation_speed));
        this.add(Bdecrease_speed);
        Bincrease_speed = new JButton("+");
        Bincrease_speed.setBounds(510, 120, 30, 20);
        Bincrease_speed.addActionListener(onIncreaseSpeedButton(Lsimulation_speed));
        this.add(Bincrease_speed);
        cb_draw_positions = new JCheckBox("Draw contact graph");
        cb_draw_positions.setBounds(510, 150, 100, 20);
        JLabel lab = new JLabel("Add Methods:");
        lab.setBounds(650, 10, 100, 20);
        add(lab);
        JRadioButton addmethod1 = new JRadioButton("R0");
        addmethod1.setBounds(650, 30, 100, 20);
        addmethod1.setActionCommand(Schwimmbad.ADDMETHOD_MODELL + "");
        addmethod1.setSelected(true);
        addmethod1.addActionListener(onRadioButton());
        JRadioButton addmethod2 = new JRadioButton("R1");
        addmethod2.setBounds(650, 60, 100, 20);
        addmethod2.setActionCommand(Schwimmbad.ADDMETHOD_R1 + "");
        addmethod2.addActionListener(onRadioButton());
        JRadioButton addmethod3 = new JRadioButton("R2");
        addmethod3.setBounds(650, 90, 100, 20);
        addmethod3.setActionCommand(Schwimmbad.ADDMETHOD_R2 + "");
        addmethod3.addActionListener(onRadioButton());
        ButtonGroup bg = new ButtonGroup();
        bg.add(addmethod1);
        bg.add(addmethod2);
        bg.add(addmethod3);
        add(addmethod1);
        add(addmethod2);
        add(addmethod3);
    }

    private ActionListener onRadioButton() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                byte value = Byte.parseByte(e.getActionCommand());
                schwimmbad.setAddmethod(value);
            }
        };
    }

    private ActionListener onAddSwimmerRandButton() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Schwimmer danzi = new Schwimmer();
                danzi.setTimeForLane(sl_time.getMinimum() + Math.random() * (sl_time.getMaximum() - sl_time.getMinimum()));
                schwimmbad.addSchwimmer(danzi);
            }
        };
    }

    private ActionListener onIncreaseSpeedButton(final JLabel lsimulation_speed2) {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Main.simulation_rate > 128) {
                    return;
                }
                Main.simulation_rate *= 2;
                lsimulation_speed2.setText(Main.simulation_rate + "x");
            }
        };
    }

    private ActionListener onDecreaseSpeedButton(final JLabel lsimulation_speed2) {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Main.simulation_rate /= 2;
                lsimulation_speed2.setText(Main.simulation_rate + "x");
            }
        };
    }

    private ChangeListener onSlideChange() {
        return new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                slider_value = ((JSlider) e.getSource()).getValue();
            }
        };
    }

    private ActionListener onAddSwimmerButton() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Schwimmer danzi = new Schwimmer();
                danzi.setTimeForLane(slider_value);
                schwimmbad.addSchwimmer(danzi);
            }
        };
    }

    private ActionListener onStartStopButton() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (simulation_running) {
                    ((JButton) e.getSource()).setText("start");
                } else {
                    ((JButton) e.getSource()).setText("stop");
                }
                simulation_running = !simulation_running;
                statistics.setPaused(!simulation_running);
                schwimmbad_gui.paintSchwimmbad();
            }
        };
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new Main();
    }
}
