package cardwall.client.view;

import com.google.gwt.user.client.ui.*;
import cardwall.shared.Task;

/**
 * @author $LastChangedBy: vogensen $
 * @version $Revision: 2 $
 */
public class CardDetailsDialog extends DialogBox {

    private Button updateButton;

    private Task task;

    private TextBox nameTextBox;

    private TextArea descriptionTextArea;

    public CardDetailsDialog() {
        super(false, false);
        Grid grid = new Grid(3, 2);
        grid.setWidget(0, 0, new Label("Name:"));
        nameTextBox = new TextBox();
        grid.setWidget(0, 1, nameTextBox);
        grid.setWidget(1, 0, new Label("Description:"));
        descriptionTextArea = new TextArea();
        grid.setWidget(1, 1, descriptionTextArea);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        updateButton = new Button("Update");
        buttonPanel.add(updateButton);
        Button closeButton = new Button("Close");
        closeButton.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                hide();
            }
        });
        buttonPanel.add(closeButton);
        grid.setWidget(2, 1, buttonPanel);
        grid.getCellFormatter().setHorizontalAlignment(2, 1, HorizontalPanel.ALIGN_RIGHT);
        add(grid);
    }

    public void setTask(Task task) {
        this.task = task;
        nameTextBox.setText(task.getName());
        descriptionTextArea.setText(task.getDescription());
    }

    public void addUpdateButtonClickListener(ClickListener clickListener) {
        updateButton.addClickListener(clickListener);
    }

    public void show() {
        setPopupPosition(100, 100);
        super.show();
    }

    public Task getTask() {
        task.setName(nameTextBox.getText());
        task.setDescription(descriptionTextArea.getText());
        return task;
    }
}
