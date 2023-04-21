package it.greentone.gui.action;

import it.greentone.GreenTone;
import javax.inject.Inject;
import javax.swing.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
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
 * Classe di convenienza contenente tutte le azioni disponibili
 * nell'applicazione. Collega le azioni supportate dal framework BSAF con le
 * {@link Action} di Swing.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ActionProvider {

    private final ApplicationContext applicationContext;

    @Inject
    AddDocumentAction addDocument;

    @Inject
    DeleteDocumentAction deleteDocument;

    @Inject
    AddJobAction addJob;

    @Inject
    AddOperationAction addOperation;

    @Inject
    AddPersonAction addPerson;

    @Inject
    DeleteJobAction deleteJob;

    @Inject
    DeleteOperationAction deleteOperation;

    @Inject
    DeletePersonAction deletePerson;

    @Inject
    EditJobCategoryAction editJobCategory;

    @Inject
    EditJobStakeholderAction editJobStakeholder;

    @Inject
    EditUserAction editUser;

    @Inject
    ExitAction exitAction;

    @Inject
    SaveDocumentAction saveDocument;

    @Inject
    SaveJobAction saveJob;

    @Inject
    SaveOperationAction saveOperation;

    @Inject
    SaveOptionsAction saveOptions;

    @Inject
    SavePersonAction savePerson;

    @Inject
    ViewDocumentsAction viewDocuments;

    @Inject
    ViewHelpAction viewHelp;

    @Inject
    ViewHomeAction viewHome;

    @Inject
    ViewJobsAction viewJobs;

    @Inject
    ViewOperationsAction viewOperations;

    @Inject
    ViewOptionsAction viewOptions;

    @Inject
    ViewPersonsAction viewPersons;

    @Inject
    ViewAboutAction viewAbout;

    @Inject
    ViewCalcAction viewCalc;

    @Inject
    ViewJobAction viewJob;

    @Inject
    ViewPersonAction viewPerson;

    @Inject
    ViewReportsAction viewReports;

    /**
	 * Oggetto di convenienza contenente tutte le azioni disponibili
	 * nell'applicazione. Collega le azioni supportate dal framework BSAF con le
	 * {@link Action} di Swing.
	 */
    public ActionProvider() {
        applicationContext = Application.getInstance(GreenTone.class).getContext();
    }

    /**
	 * Restituisce l'azione di aggiunta di una persona in anagrafica.
	 * 
	 * @return azione di aggiunta di una persona in anagrafica
	 */
    public Action getAddPerson() {
        return applicationContext.getActionMap(addPerson).get("addPerson");
    }

    /**
	 * Restituisce l'azione di rimozione di una persona in anagrafica.
	 * 
	 * @return azione di rimozione di una persona in anagrafica
	 */
    public Action getDeletePerson() {
        return applicationContext.getActionMap(deletePerson).get("deletePerson");
    }

    /**
	 * Restituisce l'azione di gestione delle categorie di un incarico.
	 * 
	 * @return azione di gestione delle categorie di un incarico
	 */
    public Action getEditJobCategory() {
        return applicationContext.getActionMap(editJobCategory).get("editJobCategory");
    }

    /**
	 * Restituisce l'azione di gestione delle persone interessate ad un incarico.
	 * 
	 * @return azione di gestione delle persone interessate ad un incarico
	 */
    public Action getEditJobStakeholder() {
        return applicationContext.getActionMap(editJobStakeholder).get("editJobStakeholder");
    }

    /**
	 * Restituisce l'azione di gestione della login di una persona.
	 * 
	 * @return l'azione di gestione della login di una persona
	 */
    public Action getEditUser() {
        return applicationContext.getActionMap(editUser).get("editUser");
    }

    /**
	 * Restituisce l'azione di chiusura dell'applicazione.
	 * 
	 * @return l'azione di chiusura dell'applicazione
	 */
    public Action getExit() {
        return applicationContext.getActionMap(exitAction).get("exit");
    }

    /**
	 * Restituisce l'azione di salvataggio di una persona in anagrafica.
	 * 
	 * @return l'azione di salvataggio di una persona in anagrafica
	 */
    public Action getSavePerson() {
        return applicationContext.getActionMap(savePerson).get("savePerson");
    }

    /**
	 * Restituisce l'azione di visualizzazione delle informazioni
	 * sull'applicazione.
	 * 
	 * @return l'azione di visualizzazione delle informazioni sull'applicazione
	 */
    public Action getViewAbout() {
        return applicationContext.getActionMap(viewAbout).get("viewAbout");
    }

    /**
	 * Restituisce l'azione di visualizzazione degli incarichi.
	 * 
	 * @return l'azione di visualizzazione degli incarichi
	 */
    public Action getViewJobs() {
        return applicationContext.getActionMap(viewJobs).get("viewJobs");
    }

    /**
	 * Restituisce l'azione di visualizzazione delle persone in anagrafica.
	 * 
	 * @return l'azione di visualizzazione delle persone in anagrafica
	 */
    public Action getViewPersons() {
        return applicationContext.getActionMap(viewPersons).get("viewPersons");
    }

    /**
	 * Restituisce l'azione di aggiunta di un incarico.
	 * 
	 * @return azione di aggiunta di un incarico
	 */
    public Action getAddJob() {
        return applicationContext.getActionMap(addJob).get("addJob");
    }

    /**
	 * Restituisce l'azione di rimozione di un incarico.
	 * 
	 * @return azione di rimozione di un incarico
	 */
    public Action getDeleteJob() {
        return applicationContext.getActionMap(deleteJob).get("deleteJob");
    }

    /**
	 * Restituisce l'azione di salvataggio di un incarico.
	 * 
	 * @return azione di salvataggio di un incarico
	 */
    public Action getSaveJob() {
        return applicationContext.getActionMap(saveJob).get("saveJob");
    }

    /**
	 * Restituisce l'azione di visualizzazione dei documenti.
	 * 
	 * @return l'azione di visualizzazione dei documenti
	 */
    public Action getViewDocuments() {
        return applicationContext.getActionMap(viewDocuments).get("viewDocuments");
    }

    /**
	 * Restituisce l'azione di aggiunta di un documento.
	 * 
	 * @return azione di aggiunta di un documento
	 */
    public Action getAddDocument() {
        return applicationContext.getActionMap(addDocument).get("addDocument");
    }

    /**
	 * Restituisce l'azione di salvataggio di un documento.
	 * 
	 * @return azione di salvataggio di un documento
	 */
    public Action getSaveDocument() {
        return applicationContext.getActionMap(saveDocument).get("saveDocument");
    }

    /**
	 * Restituisce l'azione di visualizzazione delle operazioni.
	 * 
	 * @return l'azione di visualizzazione delle operazioni
	 */
    public Action getViewOperations() {
        return applicationContext.getActionMap(viewOperations).get("viewOperations");
    }

    /**
	 * Restituisce l'azione di visualizzazione delle opzioni.
	 * 
	 * @return l'azione di visualizzazione delle opzioni
	 */
    public Action getViewOptions() {
        return applicationContext.getActionMap(viewOptions).get("viewOptions");
    }

    /**
	 * Restituisce l'azione di aggiunta di un'operazione.
	 * 
	 * @return azione di aggiunta di un'operazione
	 */
    public Action getAddOperation() {
        return applicationContext.getActionMap(addOperation).get("addOperation");
    }

    /**
	 * Restituisce l'azione di rimozione di un'operazione.
	 * 
	 * @return azione di rimozione di un'operazione
	 */
    public Action getDeleteOperation() {
        return applicationContext.getActionMap(deleteOperation).get("deleteOperation");
    }

    /**
	 * Restituisce l'azione di salvataggio di un'operazione.
	 * 
	 * @return azione di salvataggio di un'operazione
	 */
    public Action getSaveOperation() {
        return applicationContext.getActionMap(saveOperation).get("saveOperation");
    }

    /**
	 * Restituisce l'azione di salvataggio delle opzioni.
	 * 
	 * @return l'azione di salvataggio delle opzioni
	 */
    public Action getSaveOptions() {
        return applicationContext.getActionMap(saveOptions).get("saveOptions");
    }

    /**
	 * Restituisce l'azione di rimozione di un documento.
	 * 
	 * @return azione di rimozione di un documento
	 */
    public Action getDeleteDocument() {
        return applicationContext.getActionMap(deleteDocument).get("deleteDocument");
    }

    /**
	 * Restituisce l'azione che visualizza la calcolatrice.
	 * 
	 * @return azione che visualizza la calcolatrice
	 */
    public Action getViewCalc() {
        return applicationContext.getActionMap(viewCalc).get("viewCalc");
    }

    /**
	 * Restituisce l'azione che visualizza la schermata iniziale.
	 * 
	 * @return azione che visualizza la schermata iniziale
	 */
    public Action getViewHome() {
        return applicationContext.getActionMap(viewHome).get("viewHome");
    }

    /**
	 * Restituisce l'azione di visualizzazione di un incarico e dei suoi dettagli.
	 * 
	 * @return l'azione di visualizzazione di un incarico e dei suoi dettagli
	 */
    public Action getViewJob() {
        return applicationContext.getActionMap(viewJob).get("viewJob");
    }

    /**
	 * Restituisce l'azione di visualizzazione di un cliente e dei suoi dettagli.
	 * 
	 * @return l'azione di visualizzazione di un cliente e dei suoi dettagli
	 */
    public Action getViewPerson() {
        return applicationContext.getActionMap(viewPerson).get("viewPerson");
    }

    /**
	 * Restituisce l'azione di visualizzazione del manuale utente.
	 * 
	 * @return l'azione di visualizzazione del manuale utente
	 */
    public Action getViewHelp() {
        return applicationContext.getActionMap(viewHelp).get("viewHelp");
    }

    /**
	 * Restituisce l'azione di lancio del componente di stampa.
	 * 
	 * @return l'azione di lancio del componente di stampa
	 */
    public Action getViewReports() {
        return applicationContext.getActionMap(viewReports).get("viewReports");
    }
}
