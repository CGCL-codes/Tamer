package com.csci.finalproject.agileassistant.client.WhiteBoard;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.csci.finalproject.agileassistant.client.Notecard;
import com.csci.finalproject.agileassistant.client.Postit;
import com.csci.finalproject.agileassistant.client.UserStoryCondition;

/**
 * The {@link OnDropBehavior} for the "User Stories" 
 * {@link WhiteBoardColumn} in an {@link AgileWhiteBoard} 
 * @param whiteBoard
 */
public class OnDropBehavior_UserStories extends OnDropBehavior {

    public OnDropBehavior_UserStories(AbstractWhiteBoard whiteBoard) {
        super(whiteBoard);
    }

    @Override
    public void onDrop(DragContext context) {
        Notecard nc = (Notecard) context.draggable;
        if (nc.getCondition() != UserStoryCondition.WB) {
            nc.setCondition(UserStoryCondition.WB);
            for (Postit p : nc.getPostits()) {
                wb.add(p);
            }
        }
        wb.getProject().persistUserStory(nc, -1);
    }
}
