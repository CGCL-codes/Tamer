package com.nzep.display.panel;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.nzep.database.Campaign;
import com.nzep.database.DataBase;
import com.nzep.display.MainLayout;
import com.nzep.display.ToastStatusNotification;
import com.nzep.main.NetZeppelin;
import com.nzep.main.NetZeppelinEvent;
import com.nzep.main.R;

/**
 * Fen�tre de nouvelle campagne
 * 
 * @author Sebastien Villemain
 *
 */
public class NewCampaign extends MainLayout {

    /**
	 * Nom de la campagne
	 */
    private EditText campaignName = null;

    /**
	 * Description de la campagne
	 */
    private EditText campaignDescription = null;

    /**
	 * Nom du client
	 */
    private EditText clientName = null;

    /**
	 * Lieu de la campagne
	 */
    private EditText campaignPlace = null;

    /**
	 * Type d'appareil photo de la campagne
	 */
    private EditText campaignDevice = null;

    /**
	 * Nouvelle fen�tre de cr�ation d'une campagne
	 * 
	 * @param context
	 */
    public NewCampaign(NetZeppelin context) {
        super(context, R.layout.new_campaign);
        campaignName = (EditText) findViewById(R.id.new_campaign_name_input);
        campaignDescription = (EditText) findViewById(R.id.new_campaign_description_input);
        clientName = (EditText) findViewById(R.id.new_campaign_client_name_input);
        campaignPlace = (EditText) findViewById(R.id.new_campaign_place_input);
        campaignDevice = (EditText) findViewById(R.id.new_campaign_device_input);
    }

    /**
	 * V�rifie les champs obligatoire
	 * 
	 * @return boolean true c'est valide
	 */
    private boolean isValidInput() {
        ToastStatusNotification notification = new ToastStatusNotification(context, true);
        notification.setDuration(Toast.LENGTH_SHORT);
        String name = campaignName.getText().toString();
        String client = clientName.getText().toString();
        boolean isValid = EditCampaign.isValidInput("", name, client, notification);
        if (!isValid) {
            notification.show();
        }
        return isValid;
    }

    /**
	 * Enregistre la campagne
	 */
    private void add() {
        DataBase dataBase = DataBase.getInstance();
        dataBase.open();
        boolean campaignAdded = dataBase.insertEntryCampaign(campaignName.getText().toString(), campaignDescription.getText().toString(), clientName.getText().toString(), campaignPlace.getText().toString(), campaignDevice.getText().toString(), System.currentTimeMillis());
        if (campaignAdded) {
            long campaignId = dataBase.getLastId();
            Campaign campaign = dataBase.selectOneCampaign(campaignId);
            dataBase.close();
            context.setViewer(new Capture(context, campaign));
        } else {
            dataBase.close();
            new ToastStatusNotification(context, R.string.database_error, true).show();
        }
    }

    protected void buildingLayout() {
        context.refreshMenuManager(NetZeppelinEvent.IN_NEW_CAMPAIGN);
        Button newCampaign = (Button) findViewById(R.id.new_campaign_button);
        newCampaign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isValidInput()) {
                    add();
                }
            }
        });
    }

    protected void refreshLayout() {
    }
}
