package org.monet.docservice.docprocessor.data;

public interface QueryStore {

    public static final String INSERT_EVENTLOG = "Insert_EventLog";

    public static final String INSERT_EVENTLOG_PARAM_CREATIONTIME = "creationtime";

    public static final String INSERT_EVENTLOG_PARAM_LOGGER = "logger";

    public static final String INSERT_EVENTLOG_PARAM_PRIORITY = "priority";

    public static final String INSERT_EVENTLOG_PARAM_MESSAGE = "message";

    public static final String INSERT_EVENTLOG_PARAM_STACKTRACE = "stacktrace";

    public static final String INSERT_TEMPLATE = "Insert_Template";

    public static final String INSERT_TEMPLATE_PARAM_CODE = "code";

    public static final String INSERT_TEMPLATE_PARAM_ID_DOCUMENT_TYPE = "id_document_type";

    public static final String INSERT_TEMPLATE_PARAM_CREATED_DATE = "created_date";

    public static final String INSERT_TEMPLATE_DATA = "Insert_Template_Data";

    public static final String INSERT_TEMPLATE_DATA_PARAM_ID_TEMPLATE = "id_template";

    public static final String INSERT_TEMPLATE_DATA_PARAM_DATA = "data";

    public static final String INSERT_TEMPLATE_DATA_PARAM_CONTENT_TYPE = "content_type";

    public static final String INSERT_TEMPLATE_PART = "Insert_Template_Part";

    public static final String INSERT_TEMPLATE_PART_PARAM_ID = "id";

    public static final String INSERT_TEMPLATE_PART_PARAM_TEMPLATE = "id_template";

    public static final String INSERT_TEMPLATE_PART_PARAM_DATA = "data";

    public static final String INSERT_DOCUMENT = "Insert_Document";

    public static final String INSERT_DOCUMENT_PARAM_ID = "id";

    public static final String INSERT_DOCUMENT_PARAM_ID_TEMPLATE = "id_template";

    public static final String INSERT_DOCUMENT_PARAM_STATE = "state";

    public static final String SELECT_DOCUMENT = "Select_Document";

    public static final String SELECT_DOCUMENT_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_DOCUMENT_RESULTSET_ID = "id";

    public static final String SELECT_DOCUMENT_RESULTSET_STATE = "state";

    public static final String SELECT_DOCUMENT_RESULTSET_TEMPLATE_CODE = "code_template";

    public static final String SELECT_DOCUMENT_RESULTSET_TEMPLATE_ID = "id_template";

    public static final String SELECT_DOCUMENT_RESULTSET_IS_DEPRECATED = "is_deprecated";

    public static final String SELECT_DOCUMENT_METADATA = "Select_Document_Metadata";

    public static final String SELECT_DOCUMENT_METADATA_PARAM_ID_DOCUMENT = "id_document";

    ;

    public static final String SELECT_DOCUMENT_METADATA_RESULTSET_PAGE = "page";

    public static final String SELECT_DOCUMENT_METADATA_RESULTSET_WIDTH = "width";

    public static final String SELECT_DOCUMENT_METADATA_RESULTSET_HEIGHT = "height";

    public static final String SELECT_DOCUMENT_METADATA_RESULTSET_ASPECT_RATIO = "aspect_ratio";

    public static final String DELETE_DOCUMENT_PREVIEW_DATA = "Delete_Document_Preview_Data";

    public static final String DELETE_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT = "id_document";

    public static final String DELETE_DOCUMENT_DATA = "Delete_Document_Data";

    public static final String DELETE_DOCUMENT_DATA_ID_DOCUMENT = "id_document";

    public static final String DELETE_DOCUMENT = "Delete_Document";

    public static final String DELETE_DOCUMENT_ID_DOCUMENT = "id_document";

    public static final String SELECT_DOCUMENT_DATA_CONTENTTYPE = "Select_Document_Data_ContentType";

    public static final String SELECT_DOCUMENT_DATA_CONTENTTYPE_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_DOCUMENT_DATA_CONTENTTYPE_RESULTSET_CONTENTTYPE = "content_type";

    public static final String SELECT_NUMBER_OF_DOCUMENTS_WITH_ID = "Select_Number_Of_Documents_With_Id";

    public static final String SELECT_NUMBER_OF_DOCUMENTS_WITH_ID_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_NUMBER_OF_DOCUMENTS_DATA_WITH_ID = "Select_Number_Of_Documents_Data_With_Id";

    public static final String SELECT_NUMBER_OF_DOCUMENTS_DATA_WITH_ID_PARAM_ID_DOCUMENT = "id_document";

    public static final String UPDATE_DOCUMENT_DATA_COMPLETE = "Update_Document_Data_Complete";

    public static final String UPDATE_DOCUMENT_DATA = "Update_Document_Data";

    public static final String UPDATE_DOCUMENT_DATA_WITH_XML_DATA = "Update_Document_Data_With_Xml_Data";

    public static final String INSERT_DOCUMENT_DATA = "Insert_Document_Data";

    public static final String INSERT_DOCUMENT_DATA_WITH_XML_DATA = "Insert_Document_Data_With_Xml_Data";

    public static final String INSERT_DOCUMENT_DATA_PARAM_ID_DOCUMENT = "id_document";

    public static final String INSERT_DOCUMENT_DATA_PARAM_DATA = "data";

    public static final String INSERT_DOCUMENT_DATA_PARAM_XML_DATA = "xml_data";

    public static final String INSERT_DOCUMENT_DATA_PARAM_CONTENT_TYPE = "content_type";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE = "Select_Document_Preview_Data_ContentType";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_PARAM_PAGE = "page";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_PARAM_TYPE = "type";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_CONTENTTYPE_RESULTSET_CONTENTTYPE = "content_type";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA = "Select_Document_Preview_Data";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_PARAM_PAGE = "page";

    public static final String SELECT_DOCUMENT_PREVIEW_DATA_PARAM_TYPE = "type";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA = "Insert_Document_Preview_Data";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT = "id_document";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_PAGE = "page";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_DATA = "data";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_TYPE = "type";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_CONTENTTYPE = "content_type";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_WIDTH = "width";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_HEIGHT = "height";

    public static final String INSERT_DOCUMENT_PREVIEW_DATA_PARAM_ASPECT_RATIO = "aspect_ratio";

    public static final String CREATE_DOCUMENT_DATA_FROM_TEMPLATE = "Create_Document_Data_From_Template";

    public static final String CREATE_DOCUMENT_DATA_FROM_TEMPLATE_PARAM_ID_DOCUMENT = "id_document";

    public static final String UPDATE_DOCUMENT = "Update_Document";

    public static final String UPDATE_DOCUMENT_PARAM_ID_DOCUMENT = "id";

    public static final String UPDATE_DOCUMENT_PARAM_STATE = "state";

    public static final String INSERT_WORK_QUEUE_ITEM = "Insert_Work_Queue_Item";

    public static final String INSERT_WORK_QUEUE_ITEM_PARAM_ID_DOCUMENT = "id_document";

    public static final String INSERT_WORK_QUEUE_ITEM_PARAM_OPERATION = "operation";

    public static final String INSERT_WORK_QUEUE_ITEM_PARAM_STATE = "state";

    public static final String INSERT_WORK_QUEUE_ITEM_PARAM_EXTRA_DATA = "extra_data";

    public static final String SELECT_WORK_QUEUE_ITEM_EXTRA_DATA = "Select_Work_Queue_Item_Extra_Data";

    public static final String SELECT_WORK_QUEUE_ITEM_EXTRA_DATA_PARAM_ID = "id";

    public static final String SELECT_WORK_QUEUE_ITEM_EXTRA_DATA_RESULTSET_EXTRA_DATA = "extra_data";

    public static final String SELECT_WORK_QUEUE_DOCUMENT_HAS_PENDING_OPERATIONS = "Select_Count_Work_Queue_Document_State";

    public static final String SELECT_WORK_QUEUE_DOCUMENT_HAS_PENDING_OPERATIONS_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_WORK_QUEUE_DOCUMENT_HAS_PENDING_OPERATIONS_RESULTSET_COUNT = "count_state";

    public static final String SELECT_DOCUMENT_ESTIMATED_TIME = "Select_Document_Estimated_Time";

    public static final String SELECT_DOCUMENT_ESTIMATED_TIME_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_DOCUMENT_ESTIMATED_TIME_RESULTSET_TIME = "time";

    public static final String RESET_WORK_QUEUE_ITEMS_IN_PROGRESS = "Reset_Work_Queue_Items_In_Progress";

    public static final String SELECT_NOT_STARTED_WORK_QUEUE_ITEMS = "Select_Not_Started_Work_Queue_Items";

    public static final String SELECT_NOT_STARTED_WORK_QUEUE_ITEMS_RESULTSET_ID = "id";

    public static final String SELECT_NOT_STARTED_WORK_QUEUE_ITEMS_RESULTSET_ID_DOCUMENT = "id_document";

    public static final String SELECT_NOT_STARTED_WORK_QUEUE_ITEMS_RESULTSET_OPERATION = "operation";

    public static final String SELECT_NOT_STARTED_WORK_QUEUE_ITEMS_RESULTSET_QUEUE_DATE = "queue_date";

    public static final String SELECT_NOT_STARTED_WORK_QUEUE_ITEMS_RESULTSET_STATE = "state";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_ERROR = "Update_Work_Queue_Item_State_To_Error";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_ERROR_PARAM_ID = "id";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_ERROR_PARAM_STATE = "state";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_ERROR_PARAM_ERROR_MSG = "error_msg";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_FINISH = "Update_Work_Queue_Item_State_To_Finish";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_FINISH_PARAM_ID = "id";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_FINISH_PARAM_STATE = "state";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_PENDING = "Update_Work_Queue_Item_State_To_Pending";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_PENDING_PARAM_ID = "id";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_PENDING_PARAM_STATE = "state";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_IN_PROGRESS = "Update_Work_Queue_Item_State_To_In_Progress";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_IN_PROGRESS_PARAM_ID = "id";

    public static final String UPDATE_WORK_QUEUE_ITEM_STATE_TO_IN_PROGRESS_PARAM_STATE = "state";

    public static final String SELECT_TEMPLATE_PART = "Select_Template_Part";

    public static final String SELECT_TEMPLATE_PART_PARAM_ID_DOCUMENT = "id_document";

    public static final String SELECT_TEMPLATE_PART_PARAM_ID_PART = "id";

    public static final String DELETE_NODE_DOCUMENTS = "Delete_Node_Documents";

    public static final String DELETE_NODE_DOCUMENTS_PARAM_ID_DOCUMENT_IMS = "id_document_ims";

    public static final String DELETE_NODE_DOCUMENTS_PARAM_ID_DOCUMENT_DMS = "id_document_dms";

    public static final String SELECT_TEMPLATE_SIGNS = "Select_Template_Signs";

    public static final String SELECT_TEMPLATE_SIGNS_PARAM_ID_TEMPLATE = "id_template";

    public static final String SELECT_TEMPLATE_SIGNS_RESULTSET_CODE = "sign_field";

    public static final String INSERT_SIGN_FIELDS = "Insert_Sign";

    public static final String INSERT_SIGN_FIELDS_PARAM_ID_TEMPLATE = "id_template";

    public static final String INSERT_SIGN_FIELDS_PARAM_SIGN_NAME = "sign_name";

    public static final String INSERT_SIGN_FIELDS_PARAM_ROLES = "roles";

    public static final String SELECT_DOCUMENT_DATA = "Select_Document_Data";

    public static final String SELECT_DOCUMENT_DATA_PARAM_DOCUMENT_ID = "id_document";

    public static final String SELECT_DOCUMENT_DATA_RESULTSET_DATA = "data";

    public static final String SELECT_DOCUMENT_XML_DATA = "Select_Document_Xml_Data";

    public static final String SELECT_DOCUMENT_XML_DATA_PARAM_DOCUMENT_ID = "id_document";

    public static final String SELECT_DOCUMENT_XML_DATA_RESULTSET_DATA = "xml_data";

    public static final String SELECT_IMAGE_DIMENSION = "Select_Image_Dimension";

    public static final String SELECT_IMAGE_DIMENSION_PARAM_ID = "id_document";

    public static final String SELECT_IMAGE_DIMENSION_PARAM_WIDTH = "width";

    public static final String SELECT_IMAGE_DIMENSION_PARAM_HEIGHT = "height";

    String get(String key);
}
