package com.google.code.javastorage.cli.script;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Automated test script for wuala
 * 
 * @author thomas.scheuchzer@gmail.com
 * 
 */
public class S3Script extends AbstractScript {

    @Override
    public void run() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("keys.properties"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String accessKey = props.getProperty("s3.aws_access_key_id");
        String secretKey = props.getProperty("s3.aws_secret_access_key");
        execute("connect s3", accessKey, secretKey);
        execute("ls");
        execute("ls -l");
        execute("cd celli-public");
        execute("ls");
        execute("ls -l");
        execute("cd foo");
        execute("ls");
        execute("cd bar");
        execute("ls");
        execute("ls -lh");
        execute("get 1");
        execute("open 1");
        sleep(5000);
        execute("quit");
    }
}
