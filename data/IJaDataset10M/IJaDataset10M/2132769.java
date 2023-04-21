package com.w20e.socrates.factories;

import java.net.URI;
import com.w20e.socrates.model.Questionnaire;

/**
 * @Version $Revision: 1.3 $
 *
 * The QuestionnaireFactory defines the interface for factories creating models
 * (Instance/Bind combinations) and rendering info for a given questionnaire.
 *
 * @author dokter
 */
public interface QuestionnaireFactory {

    /**
   * Create the Questionnaire identified by a URI. The URI specifies the
   * protocol to retrieve the form definition, and all possible parts to
   * uniquely identify the definition for this protocol. In case of the file
   * protocol for example, the URI should look like:
   * <code>file:[path to file]</code>.
   *
   * @param url Check comment above
   * @return an initialized Questionnaire, consisting of a Model, Instance and
   * rendering configuration.
   * @throws UnsupportedProtocolException when the protocol given through
   *         the URI is not supported.
   * @throws NotFoundException when the questionnaire is not found at the given location
   * @throws InvalidException When the questionnaire is invalid
   */
    Questionnaire createQuestionnaire(URI uri) throws UnsupportedProtocolException, NotFoundException, InvalidException;
}
