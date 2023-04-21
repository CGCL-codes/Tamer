package com.apelon.dtswf.data;

import java.util.Date;

/**
 * Interface for the Assignment definition.
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Apelon Inc.
 * @since 3.2
 */
public interface Assignment {

    /**
   * Returns the ID of the Assignment.
   *
   * @return  The ID of the assignment.
   */
    long getId();

    /**
   * Returns the name of the Assignment.
   *
   * @return  The name of the Assignment.
   */
    String getName();

    /**
    * Returns the name of the Assignment.
    *
    * @return  The name of the Assignment.
    */
    String getCreatedBy();

    /**
    * Returns the name of the user to whom the Assignment is assigned.
    *
    * @return  The name of the user.
    */
    String getAssignedTo();

    /**
   * Returns the date the Assignment was created.
   *
   * @return  Assignment creation date.
   */
    Date getCreatedDate();

    /**
   * Returns the date the Assignment is due.
   *
   * @return  Assignment due date.
   */
    Date getDueDate();

    /**
    * Returns the status of the Assignment.
    * See {@link AssignmentStatus AssignmentStatus} for various Assignment statuses.
    *@return  The status of the Assignment.
    */
    String getStatus();

    /**
   * Returns the status date of the Assignment.
   *
   * @return  Assignment status date.
   */
    Date getStatusDate();

    /**
   * Returns the date the Assignment was modified.
   *
   * @return  Assignment modification date.
   */
    Date getModifiedDate();

    /**
   * Returns an array of the {@link AssignmentItem AssignmentItems} for this Assignment.
   *
   * @return  an array of the {@link AssignmentItem AssignmentItems} .
   */
    AssignmentItem[] getAssignmentItems();

    /**
   * Returns an array of the {@link NoteItem NoteItems} for this Assignment.
   *
   * @return  an array of the {@link NoteItem NoteItems} .
   */
    NoteItem[] getNotes();

    /**
   * Returns an array of the {@link AssignmentStatus AssignmentStatuses} for this Assignment.
   *
   * @return  an array of the {@link AssignmentStatus AssignmentStatuses} .
   */
    AssignmentStatus[] getStatusHistory();

    /**
    * Returns the name of the Assignment.
    *
    * @return  The name of the Assignment.
    */
    String getAssignedBy();

    /**
   * Returns the date the Assignment was assigned.
   *
   * @return  Assignment assigned date.
   */
    Date getAssignedDate();

    int getItemCount();

    int getCompletedItemCount();
}
