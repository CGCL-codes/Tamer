package org.deft.representation.listing;

import java.util.LinkedList;
import java.util.List;
import org.deft.representation.styleddocument.StyledDocumentRepresentation;

public class StyledDocumentSimpleListingRepresentation implements StyledDocumentListingRepresentation {

    public static final String TYPE = "org.deft.representation.styleddocumentlisting";

    private List<StyledDocumentRepresentation> listItems;

    public StyledDocumentSimpleListingRepresentation(List<StyledDocumentRepresentation> listItems) {
        this.listItems = listItems;
    }

    public List<StyledDocumentRepresentation> getListItems() {
        return new LinkedList<StyledDocumentRepresentation>(listItems);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
