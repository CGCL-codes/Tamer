package org.apache.webbeans.test.component.exception;

import javax.webbeans.New;
import javax.webbeans.Production;
import org.apache.webbeans.test.component.service.IService;

@Production
public class NewComponentInterfaceComponent {

    @New
    IService src;
}
