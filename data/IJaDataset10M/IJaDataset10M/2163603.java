package de.mpicbg.buchholz.phenofam.client;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class PhenoFamDataProvider {

    public interface GlobalFailureHandler {

        public void handleFailure(String method, Throwable caught, FailureAcceptor acceptor);
    }

    public interface FailureAcceptor {

        public void onFailure(Throwable caught);
    }

    public interface CalculatedDataAcceptor extends FailureAcceptor {

        public void onSuccess(ResultData result);
    }

    public interface SupportedOrganismsDataAcceptor extends FailureAcceptor {

        public void onSuccess(Set<String> supportedOrganisms);
    }

    public interface SupportedIdTypesDataAcceptor extends FailureAcceptor {

        public void onSuccess(Set<DataType> supportedDataTypes);
    }

    interface UserDataAcceptor extends FailureAcceptor {

        public void onSuccess(DataRef ref, List<InputDataEntity> selectedInpuData);
    }

    interface AvailableListsDataAcceptor extends FailureAcceptor {

        public void onSuccess(Map<DataRef, DataStatus> lists);
    }

    interface DatabaseVersionAcceptor extends FailureAcceptor {

        public void onSuccess(DatabaseVersion databaseVersion);
    }

    interface VoidAcceptor extends FailureAcceptor {

        public void onSuccess();
    }

    interface BooleanAcceptor extends FailureAcceptor {

        public void onSuccess(boolean result);
    }

    private PhenoFamServiceAsync serviceAsync;

    private GlobalFailureHandler failureHandler = new GlobalFailureHandler() {

        public void handleFailure(String method, Throwable caught, FailureAcceptor acceptor) {
            acceptor.onFailure(caught);
        }
    };

    public PhenoFamDataProvider() {
        serviceAsync = GWT.create(PhenoFamService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceAsync;
        String moduleRelativeUrl = GWT.getModuleBaseURL() + "phenofam";
        target.setServiceEntryPoint(moduleRelativeUrl);
    }

    public void setFailureHandler(GlobalFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    public void executeGetAvailableLists(final AvailableListsDataAcceptor acceptor) {
        serviceAsync.getAvailableLists(new AsyncCallback<Map<DataRef, DataStatus>>() {

            public void onSuccess(Map<DataRef, DataStatus> lists) {
                acceptor.onSuccess(lists);
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("getAvailableLists", caught, acceptor);
            }
        });
    }

    public void executeRemoveList(final DataRef ref, final AvailableListsDataAcceptor acceptor) {
        serviceAsync.removeList(ref, new AsyncCallback<Map<DataRef, DataStatus>>() {

            public void onSuccess(Map<DataRef, DataStatus> lists) {
                acceptor.onSuccess(lists);
                Tracker.trackOnDataDeleted();
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("removeList", caught, acceptor);
            }
        });
    }

    public void executeGetSupportedOrganisms(final SupportedOrganismsDataAcceptor acceptor) {
        serviceAsync.getSupportedOrganisms(new AsyncCallback<Set<String>>() {

            public void onSuccess(Set<String> result) {
                acceptor.onSuccess(result);
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("getSupportedOrganisms", caught, acceptor);
            }
        });
    }

    public void executeGetSupportedIdTypes(final String organism, final SupportedIdTypesDataAcceptor acceptor) {
        serviceAsync.getSupportedDataTypes(organism, new AsyncCallback<Set<DataType>>() {

            public void onSuccess(Set<DataType> result) {
                acceptor.onSuccess(result);
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("getSupportedDataTypes", caught, acceptor);
            }
        });
    }

    public void executeGetUserInput(final DataRef ref, final List<String> pfamIds, final UserDataAcceptor acceptor) {
        serviceAsync.getUserInput(ref, pfamIds, new AsyncCallback<List<InputDataEntity>>() {

            public void onSuccess(List<InputDataEntity> result) {
                acceptor.onSuccess(ref, result);
                Tracker.trackOnDetailedData(pfamIds);
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("getUserInput", caught, acceptor);
            }
        });
    }

    public void executeSubmitCalculation(final DataRef ref, final VoidAcceptor acceptor) {
        serviceAsync.submitCalculation(ref, new AsyncCallback<Void>() {

            public void onSuccess(Void v) {
                acceptor.onSuccess();
                Tracker.trackOnExecuteFinished();
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("submitCalculation", caught, acceptor);
            }
        });
    }

    public void executeCancelCalculation(final DataRef ref, final VoidAcceptor acceptor) {
        serviceAsync.cancelCalculation(ref, new AsyncCallback<Void>() {

            public void onSuccess(Void v) {
                acceptor.onSuccess();
                Tracker.trackOnExecuteCanceled();
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("cancelCalculation", caught, acceptor);
            }
        });
    }

    public void executeIsResultReady(final DataRef ref, final BooleanAcceptor acceptor) {
        serviceAsync.isResultReady(ref, new AsyncCallback<Boolean>() {

            public void onSuccess(Boolean result) {
                acceptor.onSuccess(result);
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("isResultReady", caught, acceptor);
            }
        });
    }

    public void executeSendResultByMail(final DataRef ref, final String mail, final VoidAcceptor acceptor) {
        serviceAsync.sendResultByMail(ref, mail, new AsyncCallback<Void>() {

            public void onSuccess(Void v) {
                acceptor.onSuccess();
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("sendResultByMail", caught, acceptor);
            }
        });
    }

    public void executeGetResult(final DataRef ref, final CalculatedDataAcceptor acceptor) {
        serviceAsync.getResult(ref, new AsyncCallback<ResultData>() {

            public void onSuccess(ResultData result) {
                acceptor.onSuccess(result);
                if (result != null) Tracker.trackOnExecuteFinished();
            }

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("getResult", caught, acceptor);
            }
        });
    }

    public void executeGetDatabaseVersion(final DatabaseVersionAcceptor acceptor) {
        serviceAsync.getDatabaseVersion(new AsyncCallback<DatabaseVersion>() {

            public void onFailure(Throwable caught) {
                failureHandler.handleFailure("getDatabaseVersion", caught, acceptor);
            }

            public void onSuccess(DatabaseVersion result) {
                acceptor.onSuccess(result);
            }
        });
    }
}
