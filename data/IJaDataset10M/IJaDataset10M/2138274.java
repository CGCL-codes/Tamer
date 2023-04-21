package staticVariables;

import staticVariables.emptyPackage.subSubPackage.StaticFriend9;

public class Static9 {

    static int f = 0;

    public static void main() {
        f = f + 1;
        StaticFriend9.f = StaticFriend9.f + 1;
        staticVariables.emptyPackage.subSubPackage.StaticFriend9.f = staticVariables.emptyPackage.subSubPackage.StaticFriend9.f + 1;
        System.out.println(f);
        System.out.println(StaticFriend9.f);
    }
}
