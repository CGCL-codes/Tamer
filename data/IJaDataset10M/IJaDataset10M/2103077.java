package com.face.api.client.response;

import java.util.List;
import com.face.api.client.model.Photo;

public interface PhotoResponse extends BaseResponse {

    public List<Photo> getPhotos();

    public Photo getPhoto();
}
