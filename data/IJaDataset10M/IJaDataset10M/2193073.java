package org.jazzteam.testEngine.main;

import java.util.LinkedList;
import org.jazzteam.testEngine.config.Config;
import org.jazzteam.testEngine.task.TaskItem;
import org.jazzteam.testEngine.task.TasksList;

/**
 * 
 * 
 * @author Noxt
 * @version $Rev: $
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello!");
        Config.Configure();
        LinkedList<TaskItem> tasksList = TasksList.getTasksList();
        for (TaskItem taskItem : tasksList) {
            System.out.println("----------------------------------------------------------");
            System.out.println("�\t\t" + taskItem.getTaskId());
            System.out.println("Title\t\t" + taskItem.getTaskTitle());
            System.out.println("Text\t\t" + taskItem.getTaskText());
            System.out.println("Author\t\t" + taskItem.getTaskAuthor());
            System.out.println("TimeLimit\t" + taskItem.getTaskTimeLimit());
        }
        System.out.println("End!");
    }
}
