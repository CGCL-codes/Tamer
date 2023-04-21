package com.google.gwt.sample.mobilewebapp.presenter.tasklist;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * The place in the app that shows a list of tasks.
 */
public class TaskListPlace extends Place {

    /**
   * The tokenizer for this place. TaskList doesn't have any state, so we don't
   * have anything to encode.
   */
    @Prefix("tl")
    public static class Tokenizer implements PlaceTokenizer<TaskListPlace> {

        public TaskListPlace getPlace(String token) {
            return new TaskListPlace(true);
        }

        public String getToken(TaskListPlace place) {
            return "";
        }
    }

    private final boolean taskListStale;

    /**
   * Construct a new {@link TaskListPlace}.
   * 
   * @param taskListStale true if the task list is stale and should be cleared
   */
    public TaskListPlace(boolean taskListStale) {
        this.taskListStale = taskListStale;
    }

    /**
   * Check if the task list is stale and should be cleared.
   * 
   * @return true if stale, false if not
   */
    public boolean isTaskListStale() {
        return taskListStale;
    }
}
