package android.bluebox.model;

import android.app.Activity;
import android.os.Bundle;

public class InitConfiguration extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pwd = getIntent().getExtras().getString("pwd");
        String remind = getIntent().getExtras().getString("remind");
        StaticBox.dbBox.createSystemEmptyRecord();
        StaticBox.dbBox.updateSystemHash(Crypto3.createMD5(pwd));
        StaticBox.passwordCrypto = new PasswordCrypto();
        StaticBox.keyCrypto = new KeyCrypto();
        StaticBox.passwordCrypto.firstInit(pwd);
        StaticBox.keyCrypto.initRandomKey(StaticBox.passwordCrypto);
        StaticBox.dbBox.updateSystemRemind(remind);
        StaticBox.makeToast(this, "Congratilation");
        this.setResult(RESULT_OK);
        finish();
    }
}
