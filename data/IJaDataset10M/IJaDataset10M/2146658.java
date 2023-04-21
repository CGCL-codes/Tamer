package transport;

/**
 * 
 */
public class GetLocalAddressResponse {

    private LocalAddress address;

    public GetLocalAddressResponse(LocalAddress addr) {
        super();
        address = addr;
    }

    public LocalAddress getAddress() {
        return address;
    }
}
