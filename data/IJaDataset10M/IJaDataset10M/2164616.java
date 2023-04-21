package tubes2;

/**
 *
 * @author d_frEak
 */
public class kurcaci extends objek {

    int targetx;

    int targety;

    int wake;

    void SetWake(int status) {
        wake = status;
    }

    int GetWake() {
        return (wake);
    }

    void NextFace(int i) {
        face = face + i;
        if (face > 4) face = face % 4;
    }
}
