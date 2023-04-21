package DDSLWS;

public interface OperationInvocationTopicDataReaderOperations extends DDS.DataReaderOperations {

    int read(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int sample_states, int view_states, int instance_states);

    int take(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int sample_states, int view_states, int instance_states);

    int read_w_condition(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, DDS.ReadCondition a_condition);

    int take_w_condition(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, DDS.ReadCondition a_condition);

    int read_next_sample(DDSLWS.OperationInvocationTopicHolder received_data, DDS.SampleInfoHolder sample_info);

    int take_next_sample(DDSLWS.OperationInvocationTopicHolder received_data, DDS.SampleInfoHolder sample_info);

    int read_instance(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, long a_handle, int sample_states, int view_states, int instance_states);

    int take_instance(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, long a_handle, int sample_states, int view_states, int instance_states);

    int read_next_instance(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, long a_handle, int sample_states, int view_states, int instance_states);

    int take_next_instance(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, long a_handle, int sample_states, int view_states, int instance_states);

    int read_next_instance_w_condition(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, long a_handle, DDS.ReadCondition a_condition);

    int take_next_instance_w_condition(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, long a_handle, DDS.ReadCondition a_condition);

    int return_loan(DDSLWS.OperationInvocationTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq);

    int get_key_value(DDSLWS.OperationInvocationTopicHolder key_holder, long handle);

    long lookup_instance(DDSLWS.OperationInvocationTopic instance);
}
