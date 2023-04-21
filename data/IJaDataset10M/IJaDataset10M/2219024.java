package org.lcx.taskvision.core;

import org.lcx.taskvision.core.attribute.TaskVisionAttribute;

/**
 * @author Laurent Carbonnaux
 */
public interface ITaskVisionClient {

    /**
	 *  Google SpreadSheet url
	 */
    public static final String GOOGLE_URL = "http://spreadsheets.google.com";

    /**
	 * Url for new backlog upload from template
	 */
    public static final String GOOGLE_DOCLIST_URL = "http://docs.google.com/feeds/default/private/full";

    public static final String GOOGLE_FOLDERLIST_URL = "http://docs.google.com/feeds/default/private/full/-/folder";

    /**
	 * Repository address + ?sq=taskid%3D + Issue key = the issue's task feed address
	 */
    public static final String TASK_URL_PREFIX = "?sq=" + TaskVisionAttribute.TASKID.getSvKey() + "%3D";

    /**
	 *  SpreadSheet url prefix
	 *  http://spreadsheets.google.com/ccc?key=o16826871518600446922.7669061195529650645
	 */
    public static final String GOOGLE_SPREADSHEET_PREFIX = GOOGLE_URL + "/ccc?key=";

    /**
	 * WorkSheet list feed url prefix
	 * http://spreadsheets.google.com/feeds/list/o16826871518600446922.7669061195529650645/od6/private/full
	 */
    public static final String GOOGLE_FEED_LIST_PREFIX = GOOGLE_URL + "/feeds/list/";

    public static final String GOOGLE_WORKSHEETS_PREFIX = GOOGLE_URL + "/feeds/worksheets/";

    /**
	 * find all tasks, means taskid not blank
	 * taskid!=""
	 */
    public static final String ALL_TASKS_QUERY = TaskVisionAttribute.TASKID.getSvKey() + "!=\"\"";

    public static final String QUERY_AND = "&&";

    public static final String QUERY_OR = "||";

    public static final String QUERY_TYPE = "querytype";

    public static String TEMPLATE_ROW = "TemplateRow";

    public static String PROPPREF = "org.lcx.scrumvision.";

    public static String PROPERTY_SPREADSHEET_NAME = PROPPREF + "spreadsheet.name";

    public static String PROPERTY_TASK_CELL_FEED_URL = PROPPREF + "taskcellfeedurl.";

    public static String PROPERTY_TASK_LIST_FEED_URL = PROPPREF + "tasklistfeedurl.";

    public static String PROPERTY_TASK_WORKSHEET_LABEL = PROPPREF + "taskwslabel.";
}
