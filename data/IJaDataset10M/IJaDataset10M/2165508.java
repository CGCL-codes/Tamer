package org.fpdev.apps.rtemaster;

/**
 * EventTypes defines static integer codes for the various application events. 
 * Fired events are handled by the AdminClient class.
 * 
 * <p>All event codes are 4-digit integers, categorized as follows:
 * <br>1000 series: Map click events
 * <br>2000 series: Events from option panels and managers
 * <br>3000 series: Menu events
 * 
 * @author demory
 */
public interface EventTypes {

    public static final int MCLICK_RECENTER = 1501;

    public static final int SPECIAL_CLICK_NODE = 1151;

    public static final int SPECIAL_CLICK_LINK = 1152;

    public static final int MCLICK_AN_TRIP_START = 1301;

    public static final int MCLICK_AN_TRIP_END = 1302;

    public static final int MCLICK_AN_QUERY_NODE = 1303;

    public static final int MCLICK_AN_QUERY_LINK = 1304;

    public static final int MCLICK_RTE_TOGGLE_LINK = 1401;

    public static final int MCLICK_RTE_ADD_STOP = 1411;

    public static final int MCLICK_RTE_DELETE_STOP = 1412;

    public static final int MCLICK_RTE_ADD_TIMEPT = 1413;

    public static final int MCLICK_RTE_DELETE_TIMEPT = 1414;

    public static final int MCLICK_RTE_TOGGLE_DIVLINK = 1421;

    public static final int MCLICK_RTE_CLIP_START = 1422;

    public static final int MCLICK_RTE_CLIP_END = 1423;

    public static final int MCLICK_RTE_CLIP_DIVERSION = 1425;

    public static final int MCLICK_MAP_CREATE_NODE = 1601;

    public static final int MCLICK_MAP_DELETE_NODE = 1602;

    public static final int MCLICK_MAP_CREATE_LINK = 1611;

    public static final int MCLICK_MAP_DELETE_LINK = 1612;

    public static final int MCLICK_MAP_SPLIT_LINK = 1613;

    public static final int MCLICK_MAP_STITCH_LINKS = 1614;

    public static final int MCLICK_MAP_ADD_SHPPT = 1618;

    public static final int MCLICK_MAP_DELETE_SHPPT = 1619;

    public static final int ANPANEL_SNAPSHOT_REFRESH = 2301;

    public static final int RTEPANEL_NEW_PROVIDER = 2401;

    public static final int RTEPANEL_NEW_ROUTE = 2411;

    public static final int RTEPANEL_EDIT_RTE_ID = 2415;

    public static final int RTESHP_SHOW_ITEM = 2451;

    public static final int RTESHP_RESOLVE_ITEM = 2452;

    public static final int RTESHP_STARTNODE = 2453;

    public static final int RTESHP_ENDNODE = 2454;

    public static final int RTESHP_EDIT_LINKS = 2455;

    public static final int RTESHP_SPECIFY_PATH = 2456;

    public static final int RTESHP_APPLY_CURRENT = 2457;

    public static final int RTESHP_CLEAR_CURRENT = 2458;

    public static final int LOC_NEW_LANDMARK = 2951;

    public static final int LOC_INIT_LANDMARK = 2952;

    public static final int LOC_UPDATE_LANDMARK = 2953;

    public static final int LOC_DELETE_LANDMARK = 2954;

    public static final int FILEMENU_SWITCH_DATA_PACKAGE = 3101;

    public static final int FILEMENU_WRITE_NETWORK_CACHE = 3151;

    public static final int FILEMENU_READ_NETWORK_CACHE = 3152;

    public static final int FILEMENU_WRITE_TRANSIT_CACHE = 3153;

    public static final int FILEMENU_READ_TRANSIT_CACHE = 3154;

    public static final int FILEMENU_WRITE_NETWORK_SHP = 3155;

    public static final int FILEMENU_WRITE_NETWORK_POSTGIS = 3156;

    public static final int FILEMENU_EXPORT_LINKS_SHAPEFILE = 3161;

    public static final int FILEMENU_EXPORT_ROUTES_SHAPEFILE = 3162;

    public static final int FILEMENU_EXPORT_STATIONS_SHAPEFILE = 3163;

    public static final int FILEMENU_EXIT = 3199;

    public static final int EDITMENU_UNDO = 3201;

    public static final int EDITMENU_REDO = 3202;

    public static final int EDITMENU_CANCEL_CURRENT_OPERATION = 3205;

    public static final int RTEMENU_IMPORT_NAMES_TEXT = 3401;

    public static final int RTEMENU_AUTOLOAD_TIMETABLES = 3403;

    public static final int RTEMENU_CLEAR_TIMETABLES = 3404;

    public static final int RTEMENU_IMPORT_PATH_INTERNAL = 3405;

    public static final int RTEMENU_IMPORT_SHP = 3410;

    public static final int RTEMENU_SHOW_IMPORT_SHP_FRAME = 3411;

    public static final int RTEMENU_IMPORT_GTFS = 3420;

    public static final int RTEMENU_EXPORT_GTFS = 3421;

    public static final int MAPMENU_CLEAR_SELECTED = 3601;

    public static final int MAPMENU_DELETE_SELECTED = 3602;

    public static final int MAPMENU_EDIT_LINK_PROPERTIES = 3605;

    public static final int MAPMENU_EDIT_SCEN_LINK_TYPES = 3606;

    public static final int MAPMENU_SIMPLIFY_SHPPTS = 3607;

    public static final int MAPMENU_OFFSET_LINKS = 3608;

    public static final int MAPMENU_REVERSE_SHPPTS = 3609;

    public static final int MAPMENU_IMPORT_LINKS = 3620;

    public static final int MAPMENU_IMPORT_STREETS_WIZARD = 3621;

    public static final int MAPMENU_IMPORT_ELEV_DATA = 3622;

    public static final int MAPMENU_IMPORT_ZIP_DATA = 3623;

    public static final int VIEWMENU_MANAGE_LANDMARKS = 3701;

    public static final int VIEWMENU_MANAGE_STREETALIASES = 3702;

    public static final int DBMENU_MIGRATE = 3801;

    public static final int DBMENU_DELETE_SINGLE_LINK = 3802;

    public static final int DBMENU_INIT_BASENET_TABLES = 3811;

    public static final int DBMENU_CLEAR_BASENET_TABLES = 3812;

    public static final int DBMENU_DROP_BASENET_TABLES = 3813;

    public static final int DBMENU_INIT_NODES_TABLE = 3814;

    public static final int DBMENU_CLEAR_NODES_TABLE = 3815;

    public static final int DBMENU_DROP_NODES_TABLE = 3816;

    public static final int DBMENU_INIT_LINKS_TABLE = 3817;

    public static final int DBMENU_CLEAR_LINKS_TABLE = 3818;

    public static final int DBMENU_DROP_LINKS_TABLE = 3819;

    public static final int DBMENU_INIT_ADDR_TABLE = 3820;

    public static final int DBMENU_CLEAR_ADDR_TABLE = 3821;

    public static final int DBMENU_DROP_ADDR_TABLE = 3822;

    public static final int DBMENU_INIT_ISECT_TABLE = 3823;

    public static final int DBMENU_CLEAR_ISECT_TABLE = 3824;

    public static final int DBMENU_DROP_ISECT_TABLE = 3825;

    public static final int DBMENU_INIT_ELEV_TABLE = 3826;

    public static final int DBMENU_CLEAR_ELEV_TABLE = 3827;

    public static final int DBMENU_DROP_ELEV_TABLE = 3828;

    public static final int DBMENU_INIT_SCENLINKTYPES_TABLE = 3829;

    public static final int DBMENU_CLEAR_SCENLINKTYPES_TABLE = 3830;

    public static final int DBMENU_DROP_SCENLINKTYPES_TABLE = 3831;

    public static final int DBMENU_INIT_LANDMARKS_TABLE = 3832;

    public static final int DBMENU_CLEAR_LANDMARKS_TABLE = 3833;

    public static final int DBMENU_DROP_LANDMARKS_TABLE = 3834;

    public static final int DBMENU_CREATE_LOG_TABLES = 3851;

    public static final int DBMENU_CLEAR_LOG_TABLES = 3852;

    public static final int DBMENU_DROP_LOG_TABLES = 3853;

    public static final int DBMENU_CREATE_TRIPLINKS_TABLE = 3854;

    public static final int DBMENU_CLEAR_TRIPLINKS_TABLES = 3855;

    public static final int DBMENU_DROP_TRIPLINKS_TABLE = 3856;

    public static final int DBMENU_CREATE_PING_TABLE = 3857;

    public static final int DBMENU_CLEAR_PING_TABLE = 3858;

    public static final int DBMENU_DROP_PING_TABLE = 3859;

    public static final int HELPMENU_ABOUT = 3901;

    public static final int DEBUGMENU_GARBAGE_COLLECT = 3951;

    public static final int DEBUGMENU_ACTION = 3952;
}
