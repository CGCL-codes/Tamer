package com.eastidea.qaforum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import com.eastidea.qaforum.model.Project;
import com.eastidea.qaforum.model.User;
import com.eastidea.qaforum.service.base.BaseMaintService;

@Local
public interface ActorMaintService extends BaseMaintService {

    public Project getProjectWithDevItems(Long projectid);

    public Project getProjectWithQaItems(Long projectid);

    public Set<User> getDevNotInProject(Long projectid);

    public Set<User> addDevToProject(List<User> devIds, Long projectid);

    public void delDev(Long devid, Long projectid);

    public Set<User> getQaNotInProject(Long projectid);

    public Set<User> addQaToProject(List<User> devIds, Long projectid);

    public void delQa(Long devid, Long projectid);

    public Set<User> getDevByProjectid(Long projectid);

    public Set<User> getQaByProjectid(Long projectid);

    public ArrayList<SelectItem> getQADictionary();

    public ArrayList<SelectItem> getDevDictionary();

    public ArrayList<SelectItem> getPmDictionary();
}
