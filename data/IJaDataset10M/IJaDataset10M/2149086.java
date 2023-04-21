package com.flight.flightsService;

public class FlightsServiceProxy implements com.flight.flightsService.FlightsService_PortType {

    private String _endpoint = null;

    private com.flight.flightsService.FlightsService_PortType flightsService_PortType = null;

    public FlightsServiceProxy() {
        _initFlightsServiceProxy();
    }

    public FlightsServiceProxy(String endpoint) {
        _endpoint = endpoint;
        _initFlightsServiceProxy();
    }

    private void _initFlightsServiceProxy() {
        try {
            flightsService_PortType = (new com.flight.flightsService.FlightsService_ServiceLocator()).getflightsServiceSOAP();
            if (flightsService_PortType != null) {
                if (_endpoint != null) ((javax.xml.rpc.Stub) flightsService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint); else _endpoint = (String) ((javax.xml.rpc.Stub) flightsService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
            }
        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (flightsService_PortType != null) ((javax.xml.rpc.Stub) flightsService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    }

    public com.flight.flightsService.FlightsService_PortType getFlightsService_PortType() {
        if (flightsService_PortType == null) _initFlightsServiceProxy();
        return flightsService_PortType;
    }

    public java.lang.String reserveSeat(java.lang.String client, com.flight.flightsService.Seat seats) throws java.rmi.RemoteException {
        if (flightsService_PortType == null) _initFlightsServiceProxy();
        return flightsService_PortType.reserveSeat(client, seats);
    }

    public java.lang.String paySeat(java.lang.String seat) throws java.rmi.RemoteException {
        if (flightsService_PortType == null) _initFlightsServiceProxy();
        return flightsService_PortType.paySeat(seat);
    }

    public com.flight.flightsService.Flight[] searchFlightByDestiny(java.lang.String destiny) throws java.rmi.RemoteException {
        if (flightsService_PortType == null) _initFlightsServiceProxy();
        return flightsService_PortType.searchFlightByDestiny(destiny);
    }

    public com.flight.flightsService.Seat[] getAvailableSeats(java.lang.String flight) throws java.rmi.RemoteException {
        if (flightsService_PortType == null) _initFlightsServiceProxy();
        return flightsService_PortType.getAvailableSeats(flight);
    }
}
