package android.webkit;

/**
 * This class is simply a container for the methods used to configure WebKit's
 * mock Geolocation service for use in LayoutTests.
 * @hide
 */
public final class MockGeolocation {

    private static MockGeolocation sMockGeolocation;

    /**
     * Set the position for the mock Geolocation service.
     */
    public void setPosition(double latitude, double longitude, double accuracy) {
        nativeSetPosition(latitude, longitude, accuracy);
    }

    /**
     * Set the error for the mock Geolocation service.
     */
    public void setError(int code, String message) {
        nativeSetError(code, message);
    }

    /**
     * Get the global instance of MockGeolocation.
     * @return The global MockGeolocation instance.
     */
    public static MockGeolocation getInstance() {
        if (sMockGeolocation == null) {
            sMockGeolocation = new MockGeolocation();
        }
        return sMockGeolocation;
    }

    private static native void nativeSetPosition(double latitude, double longitude, double accuracy);

    private static native void nativeSetError(int code, String message);
}
