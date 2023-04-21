package DDSLWS;

public interface OperationInvocationTopicDataWriterOperations extends DDS.DataWriterOperations {

    long register_instance(DDSLWS.OperationInvocationTopic instance_data);

    long register_instance_w_timestamp(DDSLWS.OperationInvocationTopic instance_data, DDS.Time_t source_timestamp);

    int unregister_instance(DDSLWS.OperationInvocationTopic instance_data, long handle);

    int unregister_instance_w_timestamp(DDSLWS.OperationInvocationTopic instance_data, long handle, DDS.Time_t source_timestamp);

    int write(DDSLWS.OperationInvocationTopic instance_data, long handle);

    int write_w_timestamp(DDSLWS.OperationInvocationTopic instance_data, long handle, DDS.Time_t source_timestamp);

    int dispose(DDSLWS.OperationInvocationTopic instance_data, long instance_handle);

    int dispose_w_timestamp(DDSLWS.OperationInvocationTopic instance_data, long instance_handle, DDS.Time_t source_timestamp);

    int writedispose(DDSLWS.OperationInvocationTopic instance_data, long instance_handle);

    int writedispose_w_timestamp(DDSLWS.OperationInvocationTopic instance_data, long instance_handle, DDS.Time_t source_timestamp);

    int get_key_value(DDSLWS.OperationInvocationTopicHolder key_holder, long handle);

    long lookup_instance(DDSLWS.OperationInvocationTopic instance_data);
}
