package org.traccar.model;

/**
 * Device
 */
public class Device {

    /**
     * Id
     */
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * International mobile equipment identity (IMEI)
     */
    private String imei;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
