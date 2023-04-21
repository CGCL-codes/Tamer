package org.japura.examples.taskmanager;

import org.japura.task.Mode;
import org.japura.task.Task;
import org.japura.task.TaskManager;

public class NestedTaskExample {

    /**
   * @param args
   */
    public static void main(String[] args) {
        final Task<String> nestedTask1 = new Task<String>() {

            @Override
            protected void doInBackground() throws Exception {
                setResult("nested task 1");
            }

            @Override
            protected void done() {
                System.out.println(getResult());
            }
        };
        nestedTask1.setMode(Mode.NESTED);
        final Task<String> nestedTask2 = new Task<String>() {

            @Override
            protected void doInBackground() throws Exception {
                setResult("nested task 2");
            }

            @Override
            protected void done() {
                System.out.println(getResult());
            }
        };
        nestedTask2.setMode(Mode.NESTED);
        Task<String> task1 = new Task<String>() {

            @Override
            protected void doInBackground() throws Exception {
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        TaskManager.submit(nestedTask1);
                    }
                });
                t.start();
                TaskManager.submit(nestedTask2);
                setResult("task 1");
            }

            @Override
            protected void done() {
                System.out.println(getResult());
            }
        };
        Task<String> task2 = new Task<String>() {

            @Override
            protected void doInBackground() throws Exception {
                setResult("task 2");
            }

            @Override
            protected void done() {
                System.out.println(getResult());
            }
        };
        TaskManager.showDebugWindow();
        nestedTask1.setName("nestedTask1");
        nestedTask2.setName("nestedTask2");
        task1.setName("task1");
        task2.setName("task2");
        TaskManager.submit(task1);
        TaskManager.submit(task2);
    }
}
