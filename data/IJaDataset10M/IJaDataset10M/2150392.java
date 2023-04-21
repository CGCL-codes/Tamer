package com.pennychecker.jfx.example.mvp.view;

import com.pennychecker.jfx.example.mvp.model.User;
import com.pennychecker.jfx.example.mvp.presenter.AllUserPresenter;
import com.sun.javafx.collections.ObservableListWrapper;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Steffen Kämpke
 */
public final class AllUserView extends Parent implements AllUserPresenter.Display {

    private Label labelFirstname;

    private Label textLabelFirstname;

    private Label labelLastname;

    private Label textLabelLastname;

    private ListView<String> userListView;

    private Button buttonAddUser;

    private Button buttonEditUser;

    private Button buttonRemoveUser;

    private VBox vBox;

    private HBox userFirstnameBoxH;

    private HBox userLastnameBoxH;

    private HBox buttonBoxH;

    public AllUserView() {
        initialize();
    }

    public Node getUserList() {
        return userListView;
    }

    public Node getAddUserButton() {
        return buttonAddUser;
    }

    public Node getEditUserButton() {
        return buttonEditUser;
    }

    public Node getRemoveUserButton() {
        return buttonRemoveUser;
    }

    public int getSelectedUserIndex() {
        return userListView.getSelectionModel().getSelectedIndex();
    }

    public void setSelectedUser(User selectedUser) {
        if (null == selectedUser) {
            textLabelFirstname.setText(" - ");
            textLabelLastname.setText(" - ");
        } else {
            textLabelFirstname.setText(selectedUser.getFirstname());
            textLabelLastname.setText(selectedUser.getLastname());
        }
    }

    public void setUsers(List<User> users) {
        final ObservableList<String> observableList = new ObservableListWrapper<String>(new ArrayList<String>());
        for (int i = 0; i < users.size(); i++) {
            final User user = users.get(i);
            observableList.add(i, user.getFirstname() + " " + user.getLastname());
        }
        userListView.setItems(observableList);
    }

    public void clearSelections() {
        userListView.getSelectionModel().clearSelection();
    }

    public void setSelection(int index) {
        userListView.getSelectionModel().select(index);
    }

    public void setWarning(String string) {
    }

    public void setError(String string) {
    }

    public Parent asParent() {
        return this;
    }

    private void initialize() {
        labelFirstname = new Label("Firstname");
        labelLastname = new Label("Lastname");
        textLabelFirstname = new Label(" - ");
        textLabelLastname = new Label(" - ");
        userListView = new ListView<String>(new ObservableListWrapper<String>(new ArrayList<String>()));
        buttonAddUser = new Button("Add");
        buttonEditUser = new Button("Edit");
        buttonRemoveUser = new Button("Remove");
        buttonBoxH = new HBox(10);
        buttonBoxH.getChildren().addAll(buttonRemoveUser, buttonEditUser, buttonAddUser);
        userFirstnameBoxH = new HBox(5);
        userLastnameBoxH = new HBox(5);
        userFirstnameBoxH.getChildren().addAll(labelFirstname, textLabelFirstname);
        userLastnameBoxH.getChildren().addAll(labelLastname, textLabelLastname);
        vBox = new VBox(10);
        vBox.setPrefSize(500, 500);
        vBox.setStyle("-fx-background-color: #FBFFDB");
        vBox.getChildren().addAll(userFirstnameBoxH, userLastnameBoxH, userListView, buttonBoxH);
        this.getChildren().add(vBox);
    }
}
