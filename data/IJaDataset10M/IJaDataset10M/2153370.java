package org.gruposp2p.dnie.client.ui.widget.dynatable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import org.gruposp2p.dnie.client.dto.DocumentToSignDTO;
import org.gruposp2p.dnie.client.util.DateUtils;
import org.gruposp2p.dnie.client.util.URLConstants;

public class SearchDocumentsTableItemWidget extends Composite {

    public static final int TEXT_TITLE_LENGTH = 30;

    DocumentToSignDTO document;

    public SearchDocumentsTableItemWidget(final DocumentToSignDTO document) {
        final HorizontalPanel horizontalPanel = new HorizontalPanel();
        this.document = document;
        horizontalPanel.setStyleName("DynaTableHorizontalPanel");
        Hyperlink documentTitle = new Hyperlink("", "");
        String titleText;
        if (document.title.length() > TEXT_TITLE_LENGTH) {
            titleText = document.title.substring(0, TEXT_TITLE_LENGTH) + " ...";
        } else {
            titleText = document.title;
        }
        documentTitle.setText(titleText);
        documentTitle.setStyleName("DocumentToSignTableTitleColumn");
        documentTitle.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                URLConstants.redirect(URLConstants.getDocumentToSignUrl(document.id));
            }
        });
        horizontalPanel.add(documentTitle);
        HTML dateCreated = new HTML(DateUtils.getSimpleStringFromDate(document.dateCreated));
        dateCreated.setStyleName("DocumentToSignTableDateCreatedColumn");
        horizontalPanel.add(dateCreated);
        HTML dateBegin = new HTML(DateUtils.getSimpleStringFromDate(document.dateBegin));
        dateBegin.setStyleName("DocumentToSignTableDateBeginColumn");
        horizontalPanel.add(dateBegin);
        HTML dateEnd = new HTML(DateUtils.getSimpleStringFromDate(document.dateEnd));
        dateEnd.setStyleName("DocumentToSignTableDateEndColumn");
        horizontalPanel.add(dateEnd);
        initWidget(horizontalPanel);
    }
}
