package ru.pit.samples.annotations;

public class GetInfoHandler implements IHandler<GetInfoEvent, GetInfoResponse> {

    public static final Class event = null;

    public GetInfoResponse handle(GetInfoEvent event) {
        GetInfoResponse response = new GetInfoResponse();
        response.setInfo("Info: " + event.getQuery());
        return response;
    }
}
