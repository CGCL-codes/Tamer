package com.technosophos.rhizome.web.command.base;

import com.technosophos.rhizome.command.AbstractCommand;
import com.technosophos.rhizome.controller.ReRouteRequest;
import com.technosophos.rhizome.document.RhizomeDocument;
import com.technosophos.rhizome.document.RhizomeParseException;
import com.technosophos.rhizome.repository.DocumentNotFoundException;
import com.technosophos.rhizome.repository.RepositoryAccessException;
import com.technosophos.rhizome.repository.RhizomeInitializationException;
import com.technosophos.rhizome.RhizomeException;

/**
 * Retrieve a document for viewing.
 * @author mbutcher
 *
 */
public abstract class BaseViewDocument extends AbstractCommand {

    protected abstract RhizomeDocument getDocument() throws RhizomeException;

    protected void execute() throws ReRouteRequest {
        try {
            RhizomeDocument doc = this.getDocument();
            this.results.add(this.createCommandResult(doc));
        } catch (DocumentNotFoundException e) {
            String err = "Oops, entry not found.";
            this.results.add(this.createErrorCommandResult(err, err));
        } catch (RhizomeInitializationException e) {
            String err = "Can't connect to the repository to get your info.";
            this.results.add(this.createErrorCommandResult(err, err));
        } catch (RepositoryAccessException e) {
            String err = "The repository is unavailable.";
            this.results.add(this.createErrorCommandResult(err, err));
        } catch (RhizomeParseException e) {
            String err = "Entry file appears to be corrupt. Contact the administrator.";
            this.results.add(this.createErrorCommandResult(err, err));
        } catch (RhizomeException e) {
            String err = "Document not available: " + e.getMessage();
            String ferr = "The requested document is not available for viewing.";
            this.results.add(this.createErrorCommandResult(err, ferr));
        }
        return;
    }
}
