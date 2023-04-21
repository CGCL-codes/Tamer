package com.narirelays.ems.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

public class CustomAuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        System.out.println("Received event of type: " + event.getClass().getName() + ": " + event.toString());
    }
}
