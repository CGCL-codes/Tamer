package com.express.service;

import com.express.service.dto.AddImpedimentRequest;
import com.express.service.dto.BacklogItemAssignRequest;
import com.express.service.dto.BacklogItemDto;
import com.express.service.dto.CreateBacklogItemRequest;

/**
 *
 */
public interface BacklogItemManager {

    /**
    * Creates a new BacklogItem based on the BacklogItemDto and type in the request provided.
    * @param request with the parameters which should be used for the creation of the new BacklogItem.
    * @return BacklogItemDto containing details to be used in creating BacklogItem
    */
    BacklogItemDto createBacklogItem(CreateBacklogItemRequest request);

    /**
    * UPdates the fields in the backlogItem who's identifier matches the dto provided.
    * @param backlogItemDto who's fields should be updated.
    */
    void updateBacklogItem(BacklogItemDto backlogItemDto);

    /**
    * Removes the BackogItem identified by the id provided from the system. All attached data will
    * be lost.
    * @param id of the backlogItem to remove.
    */
    void removeBacklogItem(Long id);

    /**
    * Allows a BacklogItem to be assigned to an Iteration. If the iterationFromId field is null it
    * is assumed that the BacklogItem is being assigned from the uncommitedBacklog of it's owning
    * Project.
    * @param request containing the id of the BacklogItem, id of the originating Iteration (or null)
    * and the id of the destination Iteration.
    */
    void backlogItemAssignmentRequest(BacklogItemAssignRequest request);

    /**
    * Convenience method to allow all a Story's tasks to be marked as done.
    * @param id of the story who's tasks are all to be marked as done.
    */
    void markStoryDone(Long id);

    /**
    * Creates an Issue as an impediment for a BacklogItem adding it to the BacklogItem and the Iteration
    * @param request containing all information required to create the impediment
    */
    void addImpediment(AddImpedimentRequest request);

    /**
    * Removes the impediment from the BacklogItem provided
    * @param dto representing the BacklogItem to remove the impediment from.
    */
    void removeImpediment(BacklogItemDto dto);
}
