package scene;

import javax.media.opengl.GL;

public class CameraSpherique extends AbstractCamera {

    float theta;

    float rho;

    float ro_step, theta_step;

    /**
	 * d�finition de la position, du regard et orientation de la sc�ne
	 * @param origineX
	 * @param origineY
	 * @param origineZ
	 * @param lookAtX
	 * @param lookAtY
	 * @param lookAtZ
	 * @param upX
	 * @param upY
	 * @param upZ
	 */
    public CameraSpherique(GLScene3D _glscene3d, float origineX, float origineY, float origineZ, float lookAtX, float lookAtY, float lookAtZ) {
        super(_glscene3d, origineX, origineY, origineZ, lookAtX, lookAtY, lookAtZ);
        this.setName("Spherique");
        this.setRo_step(angleDeRotationCamera);
        this.setTheta_step(angleDeRotationCamera);
        calculerRayon();
        calcTheta();
        calcRho();
        this.setKeyHandler(new CameraSpheriqueKeyHandler(this));
        this.setMouseHandler(new CameraSpheriqueMouseHandler(this));
    }

    public float calculerRayon() {
        float x = lookAtX - eyeX;
        float y = lookAtY - eyeY;
        float z = lookAtZ - eyeZ;
        return rayon = (float) (Math.sqrt(x * x + y * y + z * z));
    }

    public float getRayon() {
        return rayon;
    }

    public void setRayon(float rayon) {
        this.rayon = rayon;
    }

    public void calcTheta() {
        float y = this.getEyeY();
        float x = this.getEyeX();
        if (x == 0 && y == 0) theta = (float) Math.PI / 2; else {
            if (x > 0 && y < 0) theta = (float) ((float) ((Math.atan(y / x))) + 2 * Math.PI);
            if (x < 0) theta = (float) ((float) ((Math.atan(y / x))) + Math.PI);
            if (x == 0 && y > 0) theta = (float) (Math.PI / 2);
            if (x == 0 && y < 0) theta = (float) (3 * Math.PI / 2); else theta = (float) (Math.atan(y / x));
        }
    }

    public float getTheta() {
        if (theta >= Math.PI) return theta = (float) ((float) Math.PI - 0.00001);
        if (theta <= 0) return (float) (0.00001f); else return (float) (theta);
    }

    public void incTheta(int signe) {
        this.setTheta(this.getTheta() + signe * this.getTheta_step());
    }

    public void incRho(int signe) {
        this.setRho(this.getRho() + signe * this.getRo_step());
    }

    public void calcRho() {
        float x = this.getEyeX();
        float z = this.getEyeZ();
        if (z == 0 && x == 0) rho = 0;
        if (z > 0 && x < 0) rho = (float) ((float) ((Math.atan(z / x))) + 2 * Math.PI);
        if (z < 0) rho = (float) ((float) ((Math.atan(z / x))) + Math.PI);
        if (z == 0 && x > 0) rho = (float) (Math.PI / 2);
        if (z == 0 && x < 0) rho = (float) (3 * Math.PI / 2); else rho = (float) (Math.atan(z / x));
    }

    public float getRho() {
        return rho % DEUXPI;
    }

    public void majEyeXYZ() {
        setEyeXYZ((float) (this.getLookAtX() + getRayon() * Math.sin(getTheta()) * Math.cos(getRho())), (float) (this.getLookAtY() + getRayon() * Math.cos(getTheta())), (float) (this.getLookAtZ() + getRayon() * Math.sin(getTheta()) * Math.sin(getRho())));
        calculerRayon();
    }

    protected float getRo_step() {
        return ro_step;
    }

    protected void setRo_step(float ro_step) {
        this.ro_step = ro_step;
    }

    protected float getTheta_step() {
        return theta_step;
    }

    protected void setTheta_step(float theta_step) {
        this.theta_step = theta_step;
    }

    private void setRho(float ro) {
        this.rho = ro;
    }

    private void setTheta(float theta) {
        this.theta = theta;
    }

    protected void goToForward() {
        float directionX = getLookAtX() - getEyeX();
        float directionY = getLookAtY() - getEyeY();
        float directionZ = getLookAtZ() - getEyeZ();
        float coef_vitesse = 0.4f;
        float norme = (float) Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
        setEyeXYZ((float) (getEyeX() + directionX / norme * coef_vitesse), (float) (getEyeY() + directionY / norme * coef_vitesse), (float) (getEyeZ() + directionZ / norme * coef_vitesse));
    }

    protected void goToBackward() {
        float directionX = getLookAtX() - getEyeX();
        float directionY = getLookAtY() - getEyeY();
        float directionZ = getLookAtZ() - getEyeZ();
        float coef_vitesse = -0.4f;
        float norme = (float) Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
        setEyeXYZ((float) (getEyeX() + directionX / norme * coef_vitesse), (float) (getEyeY() + directionY / norme * coef_vitesse), (float) (getEyeZ() + directionZ / norme * coef_vitesse));
    }
}
