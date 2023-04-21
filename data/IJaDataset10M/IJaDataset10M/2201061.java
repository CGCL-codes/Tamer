package pub.beans;

public interface UpdateHistoryBean extends PubBeanI {

    PubBeanI getSubjectBean();

    PubBeanI getAffectedBean();

    /** Returns the type of the update */
    String getType();

    String getComment();

    String getPreviousData();

    String getDateUpdated();

    UserBean getUpdatedBy();

    String getDateLastSynchronized();
}
