package application.main;

import java.awt.BorderLayout;
import javax.swing.*;
import application.controller.*;
import application.model.*;
import application.view.*;

public class Application {

    private Simulator model;

    private MenuController menuController;

    private ButtonController buttonController;

    private AbstractView view;

    private JPanel field;

    public Application() {
        this.model = new Simulator();
        this.menuController = new MenuController(this.model);
        this.buttonController = new ButtonController(this.model);
        this.view = new AbstractView(this.model);
        JFrame window = new JFrame("Foxes and Rabbits");
        window.setSize(540, 540);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setResizable(true);
        field = model.getSimulatorView().getSimulatorViewPanel();
        field.setVisible(true);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this.menuController, BorderLayout.NORTH);
        panel.add(this.buttonController, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        window.add(panel);
        window.setVisible(true);
    }

    public Simulator getSimulator() {
        return model;
    }
}
