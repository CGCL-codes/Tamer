package net.emtg.doing.view;

import java.util.Vector;
import net.emtg.doing.main.Doing;
import net.emtg.doing.main.DoingManager;
import net.emtg.doing.pomodoro.Pomodoro;
import net.emtg.doing.pomodoro.Task;
import net.emtg.doing.time.Event;
import net.emtg.doing.time.EventObserver;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.animations.Transition3D;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * 
 * @author emiguel
 */
public class DoingMainForm extends Form implements ActionListener, EventObserver {

    private static final int DOING_TITLE = 0;

    private static final int DOING_POMODORO = 1;

    private static final int DOING_BREAK = 2;

    private DoingManager controller;

    private Parent upperPanel;

    private ToDoListContainer todoListC;

    private ConfigurationContainer configC;

    private Container currentFeature;

    private Command cmdExit, cmdPlay, cmdStop;

    private Pomodoro pomodoro;

    public DoingMainForm(DoingManager controller) {
        super();
        this.controller = controller;
        this.pomodoro = controller.getPomodoro();
        updateTitle(DOING_TITLE);
        initForm();
        populate();
        initCommands();
    }

    private void initForm() {
        setLayout(new BorderLayout());
        setScrollableY(false);
        setCyclicFocus(false);
        upperPanel = new Parent();
        todoListC = new ToDoListContainer(this);
        this.addComponent(BorderLayout.NORTH, upperPanel);
        this.addComponent(BorderLayout.CENTER, todoListC);
        currentFeature = todoListC;
        configC = new ConfigurationContainer(this, controller.getConfiguration());
        upperPanel.addFeature(todoListC);
        upperPanel.addFeature(configC);
    }

    private void populate() {
        Vector list = pomodoro.getTaskList();
        if (list.isEmpty()) {
            upperPanel.setCurrentTaskLabel(GuiUtil.getLabel("doing.exception.task.list.empty"));
        } else {
            todoListC.populate(list);
        }
    }

    private void initCommands() {
        cmdExit = new Command(GuiUtil.getLabel("doing.exit"));
        this.addCommand(cmdExit);
        cmdPlay = new Command(GuiUtil.getLabel("doing.play"));
        this.addCommand(cmdPlay, 0);
        cmdStop = new Command(GuiUtil.getLabel("doing.stop"));
        this.addCommandListener(this);
    }

    public void actionPerformed(ActionEvent arg) {
        if (arg.getCommand().equals(cmdExit)) {
            exit();
        } else {
            playStop(arg);
        }
    }

    private void exit() {
        boolean exit = Dialog.show(GuiUtil.getLabel("doing.exit"), GuiUtil.getLabel("doing.exit.ask"), GuiUtil.getLabel("doing.ok"), GuiUtil.getLabel("doing.cancel"));
        if (exit) {
            controller.exit();
        }
    }

    public void updateTime(long time) {
        upperPanel.setTime(GuiUtil.formatTime(time));
    }

    public void eventTerminated(Event event) {
        if (event.getName().equals(Pomodoro.POMODORO_NAME)) {
            updateTitle(DOING_BREAK);
        } else if (event.getName().equals(Pomodoro.BREAK_NAME)) {
            upperPanel.setPomodoros(pomodoro.getPomodoros() + "");
            updateTitle(DOING_POMODORO);
        } else {
            updateTitle(DOING_TITLE);
        }
    }

    private void setTaskDone(Task t) {
        try {
            pomodoro.setTaskDone(t);
            populate();
        } catch (Exception ex) {
            ex.printStackTrace();
            Dialog.show(GuiUtil.getLabel("doing.exception.title"), GuiUtil.getLabel(ex.getMessage()), GuiUtil.getLabel("doing.ok"), null);
        }
    }

    public void viewSelectedTask(Task t) {
        viewTask(t);
    }

    private void viewTask(Task t) {
        boolean curr;
        if (pomodoro.getCurrentTask() != null) {
            curr = pomodoro.getCurrentTask().equals(t);
        } else {
            curr = false;
        }
        TaskSubform taskCont = new TaskSubform(t, curr);
        Dialog.show(Doing.TITLE + " - " + GuiUtil.getLabel("doing.task"), taskCont, new Command[] { new Command(GuiUtil.getLabel("doing.ok")) });
        if (curr != taskCont.isCurrent()) {
            updateCurrentTaskView(t);
        }
        if (taskCont.isDone()) {
            setTaskDone(t);
            if (curr || taskCont.isCurrent()) {
                updateCurrentTaskView(null);
                pomodoroStop();
            }
        }
    }

    private void updateCurrentTaskView(Task t) {
        if (t == null) {
            Vector list = pomodoro.getTaskList();
            if (list.isEmpty()) {
                upperPanel.setCurrentTaskLabel(GuiUtil.getLabel("doing.exception.task.list.empty"));
            } else {
                upperPanel.setCurrentTaskLabel(GuiUtil.getLabel("doing.task.current.default"));
            }
            return;
        }
        try {
            if (t.equals(pomodoro.getCurrentTask())) {
                pomodoro.setCurrentTask(null);
                pomodoroStop();
                updateCurrentTaskView(null);
            } else {
                pomodoro.setCurrentTask(t);
                upperPanel.setCurrentTaskLabel(t.getDescription());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Dialog.show(GuiUtil.getLabel("doing.exception.title"), GuiUtil.getLabel(ex.getMessage()), GuiUtil.getLabel("doing.ok"), null);
        }
    }

    private void playStop(ActionEvent arg) {
        if (arg.getCommand().equals(cmdPlay)) {
            pomodoroStart();
        } else {
            pomodoroStop();
        }
        this.repaint();
    }

    private void updateTitle(int doing) {
        switch(doing) {
            case DOING_TITLE:
                this.setTitle(Doing.TITLE);
                break;
            case DOING_POMODORO:
                this.setTitle(Doing.TITLE + " - " + GuiUtil.getLabel("doing.pomodoro"));
                break;
            case DOING_BREAK:
                this.setTitle(Doing.TITLE + " - " + GuiUtil.getLabel("doing.pomodoro.break"));
                break;
        }
        this.repaint();
    }

    private void pomodoroStart() {
        try {
            pomodoro.start();
            this.removeCommand(cmdPlay);
            this.addCommand(cmdStop, 0);
            upperPanel.setPomodoros(pomodoro.getPomodoros() + "");
            updateTitle(DOING_POMODORO);
        } catch (Exception ex) {
            ex.printStackTrace();
            Dialog.show(GuiUtil.getLabel("doing.exception.title"), GuiUtil.getLabel(ex.getMessage()), GuiUtil.getLabel("doing.ok"), null);
        }
    }

    private void pomodoroStop() {
        pomodoro.stop();
        this.removeCommand(cmdStop);
        this.addCommand(cmdPlay, 0);
        updateTitle(DOING_TITLE);
        upperPanel.setTime(GuiUtil.getLabel("doing.time.initial"));
    }

    public void changeFeature(Feature newFeature) {
        if (!currentFeature.equals(newFeature)) {
            this.replace(currentFeature, newFeature, Transition3D.createRotation(200, true));
            currentFeature = newFeature;
        }
    }

    public void eventStarted(Event event) {
    }
}
