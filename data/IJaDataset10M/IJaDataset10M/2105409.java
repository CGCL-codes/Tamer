package net.saim.game.shared;

public class PropertyConstants {

    /**Ontology name*/
    public static final String NAME = "name";

    /**Endpoint*/
    public static final String ENDPOINT = "endpoint";

    /**Number of displayed properties*/
    public static String NR_OF_PROPERTIES = "#properties";

    /**rdfs:label*/
    public static String PROP0 = "property0";

    /**rdf:type*/
    public static String PROP1 = "property1";

    /**ontology specific*/
    public static String PROP2 = "property2";

    /**ontology specific 2*/
    public static String PROP3 = "property3";

    /**image*/
    public static final String IMAGE = "image";

    public static final String TYPE = "type";

    public static final String TEMPLATE_TYPE_NORMAL = "normalType";

    public static final String TEMPLATE_TYPE_MAP = "mapType";

    public static final String LONGITUDE_VALUE = "<http://www.w3.org/2003/01/geo/wgs84_pos#long>";

    public static final String LONGITUDE = "Longitude";

    public static final String LATITUDE_VALUE = "<http://www.w3.org/2003/01/geo/wgs84_pos#lat>";

    public static final String LATITUDE = "Latitude";

    public static final int CONFIDENCE_POSITIVE = 1;

    public static final int CONFIDENCE_NEGATIVE = -2;

    public static final int CONFIDENCE_ERROR = -1;

    public static final int CONFIDENCE_NOT_SPECIFIED = 0;

    public static final int LIMIT_NUMBER_OF_TYPES = 3;

    public static final int LIMIT_NUMBER_OF_PROPERTIES = 7;

    public static final String SEPERATOR_PROPERTY_VALUE = " ; ";

    /**rdfs:label*/
    public static String ATTR_RDFS_LABEL = "rdfs:label";

    /**rdf:type*/
    public static String ATTR_RDF_TYPE = "rdf:type";

    /**rdf:type*/
    public static String ATTR_DB_ABSTRACT = "db:abstract";

    public static final String NO_DECLARATION = "Declaration missing";

    public static final String DB_TABLE_NAME_HIGHSCORES = "highscores";

    public static final String DB_TABLE_NAME_INSTANCES = "instances";

    public static final String DB_TABLE_NAME_LINKS = "links";

    public static final String DB_TABLE_NAME_TEMPLATES = "templates";

    public static final String DB_TABLE_NAME_LINKEDONTOLOGIES = "linked_ontologies";

    public static final String DB_TABLE_NAME_USER = "user";

    public static final String DB_TABLE_NAME_POSITIVE = "positive";

    public static final String DB_TABLE_NAME_NEGATIVE = "negative";

    public static final String DB_TABLE_LINKS_PROPERTY_ID = "ID";

    public static final String DB_TABLE_LINKS_PROPERTY_SUBJECT = "Subject";

    public static final String DB_TABLE_LINKS_PROPERTY_PREDICATE = "Predicate";

    public static final String DB_TABLE_LINKS_PROPERTY_OBJECT = "Object";

    public static final String DB_TABLE_LINKS_PROPERTY_LINKED_ONTOLOGIES = "linkedOntologies";

    public static final String DB_TABLE_LINKS_PROPERTY_CONFIDENCE = "Confidence";

    public static final String DB_TABLE_LINKS_PROPERTY_COUNTER = "Counter";

    public static final String DB_TABLE_LINKS_PROPERTY_POSITIVE = "Positive";

    public static final String DB_TABLE_LINKS_PROPERTY_NEGATIVE = "Negative";

    public static final String DB_TABLE_INSTANCES_PROPERTY_URI = "URI";

    public static final String DB_TABLE_INSTANCES_PROPERTY_PROP0 = "Property0";

    public static final String DB_TABLE_INSTANCES_PROPERTY_PROP1 = "Property1";

    public static final String DB_TABLE_INSTANCES_PROPERTY_PROP2 = "Property2";

    public static final String DB_TABLE_INSTANCES_PROPERTY_PROP3 = "Property3";

    public static final String DB_TABLE_INSTANCES_PROPERTY_ONTOLOGY = "Ontology";

    public static final String DB_TABLE_INSTANCES_PROPERTY_IMAGE = "Image";

    public static final String DB_TABLE_INSTANCES_PROPERTY_TYPE = "Type";

    public static final String DB_TABLE_INSTANCES_PROPERTY_LONG = "Longitude";

    public static final String DB_TABLE_INSTANCES_PROPERTY_LAT = "Latitude";

    public static final String DB_TABLE_HIGHSCORES_PLAYER = "Player";

    public static final String DB_TABLE_HIGHSCORES_SCORE = "Score";

    public static final String DB_TABLE_USER_ID = "UserID";

    public static final String DB_TABLE_USER_NAME = "UserName";

    public static final String DB_TABLE_POSITIVE_LINK_ID = "LinkID";

    public static final String DB_TABLE_POSITIVE_USER_ID = "UserID";

    public static final String DB_TABLE_NEGATIVE_LINK_ID = "LinkID";

    public static final String DB_TABLE_NEGATIVE_USER_ID = "UserID";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_NAME = "Name";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_ENDPOINT = "Endpoint";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_PROP0 = "Property0";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_PROP1 = "Property1";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_PROP2 = "Property2";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_PROP3 = "Property3";

    public static final String DB_TABLE_TEMPLATES_PROPERTY_IMAGE = "Image";

    public static final String DB_TABLE_LINKEDONTOLOGIES_SUBJECT = "Subject";

    public static final String DB_TABLE_LINKEDONTOLOGIES_PREDICATE = "Predicate";

    public static final String DB_TABLE_LINKEDONTOLOGIES_OBJECT = "Object";

    public static final String ATTR_FILTER = "filter";

    public static final String ATTR_IMPORTANT = "important";

    public static final String ATTR_ZOOM = "zoomFactor";

    public static final String DIFFICULTY = "difficulty";

    public static final String DIFFICULTY_EASY = "easy";

    public static final String DIFFICULTY_MEDIUM = "medium";

    public static final String DIFFICULTY_HARD = "hard";

    public static final String DB_TABLE_NAME_DIFFICULTY = "difficulty";

    public static final String DB_TABLE_DIFFICULTY_DIFFICULTY = "Difficulty";

    public static final String DB_TABLE_DIFFICULTY_LINKEDONTOLOGIES = "linkedOntologies";
}
