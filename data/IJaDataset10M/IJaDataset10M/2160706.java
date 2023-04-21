package org.pausequafe.gui.view.main;

import java.util.List;
import org.pausequafe.core.dao.MonitoredCharacterDAO;
import org.pausequafe.core.factory.CharacterListFactory;
import org.pausequafe.data.character.APIData;
import org.pausequafe.gui.view.misc.Errors;
import org.pausequafe.misc.exceptions.PQConfigException;
import org.pausequafe.misc.exceptions.PQConnectionException;
import org.pausequafe.misc.exceptions.PQException;
import org.pausequafe.misc.exceptions.PQSQLDriverNotFoundException;
import org.pausequafe.misc.exceptions.PQUserDatabaseFileCorrupted;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;

public class AddCharacterDialog extends QDialog {

    Ui_AddCharacterDialog ui = new Ui_AddCharacterDialog();

    private APIData chosenCharacter;

    public AddCharacterDialog() {
        this(null);
    }

    public AddCharacterDialog(QWidget parent) {
        super(parent);
        setupUi();
    }

    private void setupUi() {
        ui.setupUi(this);
        ui.characterLabel.setText("<i>no character chosen...</i>");
        ui.cancelButton.clicked.connect(this, "reject()");
        ui.addButton.clicked.connect(this, "accept()");
        ui.chooseCharButton.clicked.connect(this, "fetchCharacters()");
        ui.addButton.setEnabled(false);
        fillUserIDCombo();
    }

    private void fillUserIDCombo() {
        List<APIData> list = null;
        try {
            list = MonitoredCharacterDAO.getInstance().findDistinctApiData();
        } catch (PQSQLDriverNotFoundException e) {
            Errors.popSQLDriverError(this, e);
        } catch (PQUserDatabaseFileCorrupted e) {
            Errors.popUserDBCorrupt(this, e);
        }
        ui.userIDCombo.addItem("");
        for (APIData data : list) {
            ui.userIDCombo.addItem("" + data.getUserID(), data);
        }
        ui.userIDCombo.currentIndexChanged.connect(this, "updateApiKey()");
    }

    @SuppressWarnings("unused")
    private void updateApiKey() {
        try {
            if (ui.userIDCombo.currentIndex() == 0) {
                ui.apiKeyText.setText("");
            } else {
                ui.apiKeyText.setText(((APIData) ui.userIDCombo.itemData(ui.userIDCombo.currentIndex())).getApiKey());
            }
        } catch (IndexOutOfBoundsException e) {
            ui.apiKeyText.setText("");
        }
    }

    @SuppressWarnings("unused")
    private void fetchCharacters() throws PQSQLDriverNotFoundException, PQUserDatabaseFileCorrupted {
        int userID;
        String apiKey;
        try {
            if (ui.userIDCombo.currentText().equals("") || ui.apiKeyText.text().equals("")) throw new PQException();
            userID = Integer.parseInt(ui.userIDCombo.currentText());
            apiKey = ui.apiKeyText.text();
        } catch (NumberFormatException e1) {
            Errors.popError(this, "Please enter correct API details.");
            return;
        } catch (PQException e) {
            Errors.popError(this, "Please enter correct API details.");
            return;
        }
        List<APIData> characterList;
        try {
            characterList = CharacterListFactory.getCharList(userID, apiKey);
        } catch (PQConfigException e) {
            Errors.popError(this, "Configuration Error.");
            return;
        } catch (PQConnectionException e) {
            Errors.popError(this, "Could not connect to EVE-Online API server.");
            return;
        } catch (PQException e) {
            Errors.popError(this, "Wrong API Key.");
            return;
        }
        CharacterListDialog dialog = new CharacterListDialog(this, characterList);
        dialog.exec();
        if (dialog.result() == QDialog.DialogCode.Accepted.value()) {
            int index = dialog.getChosenCharacterIndex();
            if (index == -1) {
                Errors.popError(this, "Character not in the list.");
                return;
            }
            if (MonitoredCharacterDAO.getInstance().isMonitored(characterList.get(index))) {
                Errors.popError(this, "This character is already monitored.");
                return;
            }
            chosenCharacter = characterList.get(index);
            ui.characterLabel.setText("<b>" + chosenCharacter.getCharacterName() + "</b>");
            ui.addButton.setEnabled(true);
        }
    }

    public APIData getChosenCharacter() {
        return chosenCharacter;
    }
}
