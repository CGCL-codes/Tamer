package wolf.adminpanel.accesspoint.ws;

import java.rmi.RemoteException;
import javax.activation.DataHandler;
import javax.ejb.Remote;
import wolf.common.vo.ProblemVO;
import wolf.common.vo.RoleVO;
import wolf.common.vo.UserVO;

@Remote
public interface IAdminPanelAccessPoint extends java.rmi.Remote {

    DataHandler getProblemDescriptions() throws RemoteException;

    DataHandler getProblem(Integer problemId) throws RemoteException;

    void newProblem(DataHandler problem, boolean generateRulesNow) throws RemoteException;

    void updateProblem(DataHandler problem) throws RemoteException;

    void deleteProblem(Integer problemId) throws RemoteException;

    void generateRules(Integer problemId) throws RemoteException;

    void deleteUser(String userId) throws RemoteException;

    RoleVO[] getRoleArray() throws RemoteException;

    UserVO isAuthorized(String user, String password) throws RemoteException;

    UserVO[] getUserArray() throws RemoteException;

    void addUser(UserVO user) throws RemoteException;

    void updateUser(UserVO user) throws RemoteException;

    UserVO getUser(String userId) throws RemoteException;
}
