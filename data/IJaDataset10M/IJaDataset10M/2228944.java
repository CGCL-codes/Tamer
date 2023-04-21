package org.gruposp2p.dnie.client;

import com.google.gwt.i18n.client.Constants;

public interface DNIeConstants extends Constants {

    /**
       * The available style themes that the user can select.
       */
    String[] STYLE_THEMES = { "standard" };

    /**
       * @return the title of the main menu
       */
    String mainMenuTitle();

    /**
       * @return the sub title of the application
       */
    String mainSubTitle();

    /**
       * @return the title of the application
       */
    String mainTitle();

    String acceptButtonText();

    String cancelButtonText();

    String editButtonText();

    String deleteButtonText();

    String detailsButtonText();

    String closeButtonText();

    String dateBegin();

    String dateEnd();

    String optionContent();

    String votingOptionButtonCaption();

    String votingOptionPanelCaption();

    String createVotingOptionPanelCaption();

    String confirmDeleteOptionPanelCaption();

    String confirmDeleteOptionMessage();

    String deleteCaption();

    String SignatureDataDialogManifestCaption();

    String SignatureDataDialogVoteCaption();

    String SignatureDataDialogTitleLabel();

    String SignatureDataDialogContentLabel();

    String SignatureDataDialogDateLabel();

    String SignatureDataDialogOptionsLabel();

    String SignatureDataDialogCommentsLabel();

    String GlassedDialogCaption();

    String LoadingDataMessage();

    String ConnectionErrorMessage();

    String statusCodeMsg();

    String statusDescriptionMsg();

    String mainMenuBarCreateObjectCategory();

    String mainMenuBarCreateNewSignatureDocument();

    String mainMenuBarCreateNewVotingDocument();

    String mainMenuBarAdminCategory();

    String AuthenticateDialogBoxCaption();

    String AuthenticateDialogBoxRegisterCaption();

    String AuthenticateDialogCancel();

    String AuthenticateDialogRegisterText();

    String AuthenticateDialogNameLabel();

    String AuthenticateDialogSurnameLabel();

    String AuthenticateDialogEmailLabel();

    String AuthenticateDialogRegisteredText();

    String AuthenticateDialogRegister();

    String AuthenticateDialogPassword();

    String AuthenticateDialogRepeatPassword();

    String ValidationErrorDialogBoxCaption();

    String ValidationErrorMessage();

    String DocumentToSignDetailsCaption();

    String NumberOfSignatures();

    String OptionsSelected();

    String DetailsDocumenTabCaption();

    String StatisticDocumenTabCaption();

    String DocumentUrlCaption();

    String documentToSignTitleLabel();

    String documentToSignCreatedLabel();

    String documentToSignActiveFromLabel();

    String documentToSignActiveToLabel();

    String signedDocumentCaption();

    String loadingInfoLabel();
}
