package dviz.test;

import dviz.visualAspect.DeclarationTranslation;
import dviz.visualSystem.DummyAnimationSystem;

public class DummyTest {

    /**
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        DeclarationTranslation declarationTranslation = DeclarationTranslation.createByAnnotation();
        DummyAnimationSystem animationSystem = new DummyAnimationSystem();
        declarationTranslation.addAnimationSystem(animationSystem);
        String MAIN_CLASS = null;
        if (args.length > 0) MAIN_CLASS = args[0]; else MAIN_CLASS = "dviz.incubator.SimpleWorld";
        declarationTranslation.translateInvoke(MAIN_CLASS, "main", new Object[] { new String[0] });
    }
}
