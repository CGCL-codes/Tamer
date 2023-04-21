package examples.fish.s03;

import anima.annotation.ComponentInterface;
import anima.component.ISupports;

@ComponentInterface("http://purl.org/NET/dcc/examples.fish.s03.IAquariumProvided")
public interface IAquariumProvided extends ISupports {

    public void drawAquarium();
}
