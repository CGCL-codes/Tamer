package DDS;

/**
* DDS/QueryConditionOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from dds.idl
* marted� 7 novembre 2006 12.55.32 CET
*/
public interface QueryConditionOperations extends DDS.ReadConditionOperations {

    String get_query_expression();

    String[] get_query_arguments();

    int set_query_arguments(String[] query_arguments);
}
