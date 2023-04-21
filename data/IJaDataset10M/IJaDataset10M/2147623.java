package flogger.example.config;

import flogger.Config;
import static flogger.Flogger.INFO;

public class ConfigDisabledOverride {

    public static void main(String[] args) {
        Config config = Config.getInstance();
        config.disabled(true);
        System.out.println("disabled: " + config.disabled());
        INFO.log("hello world");
    }
}
