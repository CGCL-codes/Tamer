package com.mathassistant.client.service;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mathassistant.shared.TheoryDTO;
import com.mathassistant.shared.TopicDTO;

@RemoteServiceRelativePath("lesson")
public interface TheoryService extends RemoteService {

    void createTheory(TheoryDTO theory, TopicDTO topic);

    void updateTheory(TheoryDTO theory);

    void deleteTheory(TheoryDTO thoory);

    int getCount(TopicDTO topic);

    TheoryDTO getTheory(int index, TopicDTO topic);

    ArrayList<TheoryDTO> getTheorys(int index, TopicDTO topic);

    ArrayList<TheoryDTO> getTheorys(TopicDTO topic);
}
