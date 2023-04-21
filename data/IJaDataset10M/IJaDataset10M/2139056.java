package br.com.unitri.blog.client;

import java.util.ArrayList;
import br.com.unitri.blog.shared.BlogService;
import br.com.unitri.blog.shared.BlogServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Ex5View extends Composite {

    interface MyUiBinder extends UiBinder<Widget, Ex5View> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    VerticalPanel pnlPosts;

    @UiField
    TextArea txtPost;

    BlogServiceAsync blogSvc = GWT.create(BlogService.class);

    public Ex5View() {
        initWidget(uiBinder.createAndBindUi(this));
        recarregarPostagens();
    }

    private void recarregarPostagens() {
        blogSvc.pesquisar(new AsyncCall<ArrayList<String>>() {

            @Override
            public void onSuccess(ArrayList<String> result) {
                pnlPosts.clear();
                for (int i = 0; i < result.size(); i++) {
                    pnlPosts.add(new Hyperlink(result.get(i), "post&pos=" + i));
                }
            }
        });
    }

    @UiHandler("btnPost")
    public void postar(ClickEvent evt) {
        blogSvc.postar(txtPost.getText(), new AsyncCall<Void>() {

            @Override
            public void onSuccess(Void result) {
                txtPost.setText("");
                recarregarPostagens();
            }
        });
    }
}
