package com.face.api.client.response;

import java.util.List;
import com.face.api.client.model.UserStatus;

public interface TrainResponse extends BaseResponse {

    public List<UserStatus> getCreated();

    public List<UserStatus> getNoTrainingSet();

    public List<UserStatus> getUpdated();

    public List<UserStatus> getUnchanged();

    public List<UserStatus> getInProgress();
}
