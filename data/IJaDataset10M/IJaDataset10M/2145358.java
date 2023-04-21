package smashedapples.api.simpledb;

import com.amazonaws.sdb.*;
import com.amazonaws.sdb.model.*;
import smashedapples.api.simpledb.commands.DeleteItemsCommand;
import smashedapples.api.simpledb.responses.DeleteItemsResponse;

public class DeleteItemsApi {

    public DeleteItemsResponse execute(DeleteItemsCommand command) {
        AmazonSimpleDB service = new AmazonSimpleDBClient(command.awsAccessKeyId, command.awsSecretAccessKey);
        DeleteItemsResponse response = new DeleteItemsResponse();
        try {
            for (String itemID : command.itemList) {
                DeleteAttributes request = new DeleteAttributes();
                request.setItemName(itemID);
                request.setDomainName(command.collectionName);
                service.deleteAttributes(request);
            }
        } catch (AmazonSimpleDBException ex) {
            response.awsException = ex;
        }
        return response;
    }
}
