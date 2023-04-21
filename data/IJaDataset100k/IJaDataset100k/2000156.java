package com.apelon.dtswf.data;

/**
 * Interface for the AssignmentItem definition.
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Apelon Inc.
 * @since 3.2
 */
public interface AssignmentItem {

    int EDIT_ACTION_NONE = 0;

    int EDIT_ACTION_INSERT = 1;

    int EDIT_ACTION_UPDATE = 2;

    int EDIT_ACTION_DELETE = 3;

    String ITEM_TYPE_CONCEPT = "C";

    String ITEM_TYPE_TERM = "T";

    /**
   * Returns the ID of the AssignmentItem.
   *
   * @return  The ID.
   */
    long getId();

    /**
   * Returns true if the AssignmentItem is complete.
   *
   * @return true if complete, false if not.
   */
    boolean isComplete();

    /**
   * Returns the note on the AssignmentItem.
   *
   * @return The note string.
   */
    String getNote();

    /**
   * Returns the type of AssignmentItem. Expect {@link AssignmentItem#ITEM_TYPE_CONCEPT ITEM_TYPE_CONCEPT}
   * or {@link AssignmentItem#ITEM_TYPE_CONCEPT ITEM_TYPE_CONCEPT}.
   *
   * @return  the type of AssignmentItem.
   */
    String getItemType();

    /**
   * Returns the ID of the item of this AssignmentItem.
   *
   * @return  The ID value.
   */
    int getItemId();

    /**
   * Returns the code of the item of this AssignmentItem.
   *
   * @return  The code value.
   */
    String getItemCode();

    /**
   * Returns the name of the item of this AssignmentItem.
   *
   * @return  The name.
   */
    String getItemName();

    /**
   * Returns the namespace ID of the item of this AssignmentItem.
   *
   * @return  The namespace ID value.
   */
    int getItemNamespaceId();

    /**
   * Returns the namespace name of the item of this AssignmentItem.
   *
   * @return  The namespace name.
   */
    String getItemNamespaceName();

    /**
   * Returns the name of the user who created this AssignmentItem.
   *
   * @return  The user name.
   */
    String getCreatedBy();

    /**
   * Returns the edit action of this AssignmentItem. Expect one of the following edit actions:
   * <ul>
   * <li> {@link AssignmentItem#EDIT_ACTION_NONE EDIT_ACTION_NONE}
   * <li> {@link AssignmentItem#EDIT_ACTION_INSERT EDIT_ACTION_INSERT}
   * <li> {@link AssignmentItem#EDIT_ACTION_UPDATE EDIT_ACTION_UPDATE}
   * <li> {@link AssignmentItem#EDIT_ACTION_DELETE EDIT_ACTION_DELETE}
   * </ul>
  *
   * @return  The edit action.
   */
    int getEditAction();
}
