package pt.isel.meic.agendaagent.dummy;

import pt.isel.meic.agendaagent.UserNotifierInterface;

public class DummyUserNotifier implements UserNotifierInterface {

    @Override
    public void onNewEventProposed(String pName) {
        System.out.println("NewEventProposed: " + pName);
    }

    @Override
    public void onNewEventProposalReceive(String pName) {
        System.out.println("NewEventProposalReceive: " + pName);
    }

    @Override
    public void onNewEventAccepted(String pName) {
        System.out.println("NewEventAccepted: " + pName);
    }

    @Override
    public void onNotify(String pText) {
        System.out.println("onNotify: " + pText);
    }

    @Override
    public void onAllProposalsReceived(String pName) {
        System.out.println("onAllProposalsReceived: " + pName);
    }

    @Override
    public void onRefuseReceived(String pName) {
        System.out.println("onRefuseReceived: " + pName);
    }

    @Override
    public void onInformReceived(String pName) {
        System.out.println("onInformReceived: " + pName);
    }

    @Override
    public void onFailureReceived(String pName) {
        System.out.println("onFailureReceived: " + pName);
    }

    @Override
    public void onEventCanceled(String pName) {
        System.out.println("onEventCanceled: " + pName);
    }
}
