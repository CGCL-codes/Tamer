package com.kitten.database;

import com.kitten.database.KittenSQLCommandListener.ResultEvent;

public interface KittenCommandListener {

    public void onResult(ResultEvent e);

    public class ResultEvent extends java.util.EventObject {

        /**
		 * 
		 */
        private static final long serialVersionUID = 7273787961124720497L;

        private KittenDatabaseExecutionData executionData;

        public ResultEvent(Object source) {
            super(source);
            executionData = ((KittenExecuteCommand) source).getKittenDatabaseExecutionData();
        }

        public KittenDatabaseExecutionData getKittenDatabaseExecutionData() {
            return executionData;
        }
    }
}
