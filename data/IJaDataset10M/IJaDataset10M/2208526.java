package android.content.cts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * a basic Mock Service for wrapping the MockAccountAuthenticator
 */
public class MockAccountService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return MockAccountAuthenticator.getMockAuthenticator(this).getIBinder();
    }
}
