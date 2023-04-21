package pe.bn.service.interfaz;

public interface GatewayInterface extends java.rmi.Remote {

    public pe.bn.service.bean.ResponseGateway enviarTramaConsulta(pe.bn.service.bean.RequestGateway request) throws java.rmi.RemoteException;
}
