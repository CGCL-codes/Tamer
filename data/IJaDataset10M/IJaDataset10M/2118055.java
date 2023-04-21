package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.panel.AbstractPanel.EStatus;
import it.greentone.gui.panel.PersonsPanel;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.springframework.stereotype.Component;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code>
 * <br>
 * <br>
 * Azione di salvataggio di una persona in anagrafica.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SavePersonAction extends AbstractBean {

    @Inject
    private PersonsPanel personsPanel;

    @Inject
    private PersonService personService;

    private final ResourceMap resourceMap;

    boolean savePersonActionEnabled = false;

    /**
	 * Azione di salvataggio di una persona in anagrafica.
	 */
    public SavePersonAction() {
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
    }

    /**
	 * Rende persistente una persona in anagrafica. La ragione sociale è
	 * obbligatoria.
	 */
    @Action(enabledProperty = "savePersonActionEnabled")
    public void savePerson() {
        String name = GreenToneUtilities.getText(personsPanel.getNameTextField());
        if (name == null) {
            JOptionPane.showMessageDialog(personsPanel, resourceMap.getString("savePerson.Action.nameNotNull"), resourceMap.getString("ErrorMessage.title"), JOptionPane.ERROR_MESSAGE);
            personsPanel.getNameTextField().requestFocus();
        } else {
            Person person = personsPanel.getStatus() == EStatus.NEW ? new Person() : personsPanel.getSelectedItem();
            person.setAddress(GreenToneUtilities.getText(personsPanel.getAddressTextField()));
            person.setCap(GreenToneUtilities.getText(personsPanel.getCapTextField()));
            person.setCf(GreenToneUtilities.getText(personsPanel.getCfTexField()));
            person.setCity(GreenToneUtilities.getText(personsPanel.getCityTextField()));
            person.setEmail(GreenToneUtilities.getText(personsPanel.getEmailTextField()));
            person.setFax(GreenToneUtilities.getText(personsPanel.getFaxTextField()));
            person.setIdentityCard(GreenToneUtilities.getText(personsPanel.getIdentityCardTextField()));
            person.setLegal(personsPanel.getIsLegalCheckBox().isSelected());
            person.setName(GreenToneUtilities.getText(personsPanel.getNameTextField()));
            person.setPiva(GreenToneUtilities.getText(personsPanel.getPivaTextField()));
            person.setProvince(GreenToneUtilities.getText(personsPanel.getProvinceTextField()));
            person.setTelephone1(GreenToneUtilities.getText(personsPanel.getTelephone1TextField()));
            person.setTelephone2(GreenToneUtilities.getText(personsPanel.getTelephone2TextField()));
            if (person.getIsLegal()) {
                if (person.getPiva() == null) {
                    JOptionPane.showMessageDialog(personsPanel, resourceMap.getString("savePerson.Action.pivaNotNull"), resourceMap.getString("ErrorMessage.title"), JOptionPane.ERROR_MESSAGE);
                    personsPanel.getNameTextField().requestFocus();
                    return;
                }
            } else {
                if (person.getCf() == null) {
                    JOptionPane.showMessageDialog(personsPanel, resourceMap.getString("savePerson.Action.cfNotNull"), resourceMap.getString("ErrorMessage.title"), JOptionPane.ERROR_MESSAGE);
                    personsPanel.getNameTextField().requestFocus();
                    return;
                }
            }
            if (personsPanel.getStatus() == EStatus.NEW) {
                personService.addPerson(person);
            } else {
                personService.storePerson(person);
            }
            personsPanel.postSaveData();
        }
    }

    /**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param savePersonActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
    public void setSavePersonActionEnabled(boolean savePersonActionEnabled) {
        final boolean oldValue = this.savePersonActionEnabled;
        this.savePersonActionEnabled = savePersonActionEnabled;
        firePropertyChange("savePersonActionEnabled", oldValue, savePersonActionEnabled);
    }

    /**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
    public boolean isSavePersonActionEnabled() {
        return savePersonActionEnabled;
    }
}
