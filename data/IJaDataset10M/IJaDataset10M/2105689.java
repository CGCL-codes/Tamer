package videotoons.data;

import videotoons.game.*;

class PlayerGoemon {

    public AnimLibrary anim;

    public PlayerGoemon() {
        anim = new AnimLibrary();
        anim.name = new String("goemon");
        anim.nb_images = 28;
        int array1[] = { 2, 3, 4, 5, 2, 6, 7, 8 };
        anim.move_right = new Anim(array1);
        int array2[] = { 10, 11, 12, 13, 10, 14, 15, 16 };
        anim.move_left = new Anim(array2);
        int array3[] = { 20, 19, 18, 17, 16, 15, 13, 10, 6, 2, 1 };
        anim.jump_offset = array3;
        anim.nb_jump_steps = 11;
        int array4[] = { 9 };
        anim.leftside = new Anim(array4);
        int array5[] = { 0 };
        anim.middle = new Anim(array5);
        int array6[] = { 1 };
        anim.rightside = new Anim(array6);
        int array7[] = { 18 };
        anim.jump_left = new Anim(array7);
        int array8[] = { 17 };
        anim.jump_right = new Anim(array8);
        int array9[] = { 19, 19, 19, 21, 21, 21, 21, 21 };
        anim.hit_up_right = new Anim(array9);
        int array10[] = { 20, 20, 20, 22, 22, 22, 22, 22 };
        anim.hit_up_left = new Anim(array10);
        int array11[] = { 18 };
        anim.fall_left = new Anim(array11);
        int array12[] = { 17 };
        anim.fall_right = new Anim(array12);
        int array13[] = { -20, -20, -15, -10, -6, -2, 0, 2, 6, 10, 15, 20, 25 };
        anim.dead_offset = array13;
        anim.nb_dead_steps = 13;
        int array14[] = { 23 };
        anim.dead = new Anim(array14);
        int array15[] = { 25 };
        anim.hurt_left = new Anim(array15);
        int array16[] = { 26 };
        anim.hurt_right = new Anim(array16);
        int array17[] = { -10, -5, -2, 0 };
        anim.hurt_offset = array17;
        anim.nb_hurt_steps = 4;
        int array18[] = { 22, 22, 27, 27, 27, 27, 9 };
        anim.hit_left = new Anim(array18);
        int array19[] = { 21, 21, 24, 24, 24, 24, 1 };
        anim.hit_right = new Anim(array19);
        int array20[] = { 0, 0, -22, 0, 0, 0, 22 };
        anim.hit_left_offset = array20;
        int array21[] = { -17, 0, 17, 0, 0, 0, 0 };
        anim.hit_right_offset = array21;
        anim.nb_hit_steps = 7;
        anim.sprite_basicspeed = 5;
        anim.overlap_sprite = 10;
        anim.std_width = 21;
        anim.std_height = 31;
        anim.hit_interval = 200;
        anim.jump_sound = SoundLibrary.JUMP_SOUND;
        anim.land_sound = SoundLibrary.LAND_SOUND;
        anim.hit_sound = SoundLibrary.LIGHT_HIT_SOUND;
        anim.hurt_sound = SoundLibrary.LONG_HURT_SOUND;
        anim.dead_sound = SoundLibrary.DEAD_SOUND;
    }
}
